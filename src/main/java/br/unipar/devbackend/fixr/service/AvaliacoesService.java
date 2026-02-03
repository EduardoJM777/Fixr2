package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.AvaliacoesRepository;
import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.Repository.PrestadorRepository;
import br.unipar.devbackend.fixr.dto.AvaliacoesDTO;
import br.unipar.devbackend.fixr.model.Avaliacoes;
import br.unipar.devbackend.fixr.model.Cliente;
import br.unipar.devbackend.fixr.model.Prestador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
    public Avaliacoes cadastrar(AvaliacoesDTO avaliacoesDTO){
        Avaliacoes avaliacoes = new Avaliacoes();
        avaliacoes.setNota(avaliacoesDTO.nota());
        avaliacoes.setComentario(avaliacoesDTO.comentario());
        avaliacoes.setSugest_melhoria(avaliacoesDTO.sugest_melhoria());

        Cliente cliente = clienteRepository.getReferenceById(avaliacoesDTO.idCliente());
        avaliacoes.setCliente(cliente);

        Prestador prestador = prestadorRepository.getReferenceById(avaliacoesDTO.idPrestador());
        avaliacoes.setPrestador(prestador);

        return repository.save(avaliacoes);
    }

    public List<Avaliacoes> listar(){
        return repository.findAll();
    }

    public Avaliacoes buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));
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
        repository.deleteById(id);
    }

}
