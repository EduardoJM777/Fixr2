package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.AvaliacoesRepository;
import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.Repository.PrestadorRepository;
import br.unipar.devbackend.fixr.dto.AvaliacoesDTO;
import br.unipar.devbackend.fixr.model.Avaliacoes;
import br.unipar.devbackend.fixr.model.Cliente;
import br.unipar.devbackend.fixr.model.Prestador;
import br.unipar.devbackend.fixr.model.UserType;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
public class AvaliacoesService {

    private final AvaliacoesRepository repository;
    private final ClienteRepository clienteRepository;
    private final PrestadorRepository prestadorRepository;

    @Autowired
    public AvaliacoesService(AvaliacoesRepository repository, ClienteRepository clienteRepository, PrestadorRepository prestadorRepository){
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.prestadorRepository = prestadorRepository;
    }

    @PostMapping
    public Avaliacoes cadastrar(AvaliacoesDTO dto) {
        // verifica se já existe avaliação
        Optional<Avaliacoes> existente = repository.findByClienteIdAndPrestadorIdAndAvaliadorTipo(
                dto.idCliente(), dto.idPrestador(), dto.avaliadorTipo()
        );

        Avaliacoes avaliacoes = existente.orElse(new Avaliacoes());

        avaliacoes.setNota(dto.nota());
        avaliacoes.setComentario(dto.comentario());
        avaliacoes.setSugest_melhoria(dto.sugest_melhoria());
        avaliacoes.setAvaliadorTipo(dto.avaliadorTipo());

        if (existente.isEmpty()) {
            // só seta cliente e prestador na criação
            avaliacoes.setCliente(clienteRepository.getReferenceById(dto.idCliente()));
            avaliacoes.setPrestador(prestadorRepository.getReferenceById(dto.idPrestador()));
        }

        return repository.save(avaliacoes);
    }

    public List<Avaliacoes> listar(){
        return repository.findAll();
    }

    public Avaliacoes buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));
    }

    public List<Avaliacoes> listarPorPrestador(Long id) {
        return repository.findByPrestadorIdAndAvaliadorTipo(id, UserType.CLIENTE);
    }

    public List<Avaliacoes> listarPorCliente(Long id) {
        return repository.findByClienteIdAndAvaliadorTipo(id, UserType.PRESTADOR);
    }

    public Avaliacoes atualizar(Long id, AvaliacoesDTO avaliacoesDTOatualizado){
        return repository.findById(id).map(avaliacoes -> {
            avaliacoes.setNota(avaliacoesDTOatualizado.nota());
            avaliacoes.setComentario(avaliacoesDTOatualizado.comentario());
            avaliacoes.setSugest_melhoria(avaliacoesDTOatualizado.sugest_melhoria());

            Cliente cliente = clienteRepository.getReferenceById(avaliacoesDTOatualizado.idCliente());
            avaliacoes.setCliente(cliente);

            Prestador prestador = prestadorRepository.getReferenceById(avaliacoesDTOatualizado.idPrestador());
            avaliacoes.setPrestador(prestador);

            return repository.save(avaliacoes);
        }).orElseThrow(() -> new RuntimeException("Não encontrado."));
    }

    public void deletar(Long id){
        Avaliacoes avaliacoes = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Avaliacao não encontrada"));

        avaliacoes.setAtivo(false);

        repository.save(avaliacoes);
    }

}
