package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.AnunciosRepository;
import br.unipar.devbackend.fixr.Repository.AvaliacoesRepository;
import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.Repository.EstatisticasRepository;
import br.unipar.devbackend.fixr.dto.ClienteDTO;
import br.unipar.devbackend.fixr.dto.EstatisticasDTO;
import br.unipar.devbackend.fixr.model.Cliente;
import br.unipar.devbackend.fixr.model.Estatisticas;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EstatisticasRepository estatisticasRepository;
    private final AvaliacoesRepository avaliacoesRepository;
    private final AnunciosRepository anunciosRepository;

    @Autowired
    public ClienteService(ClienteRepository repository,
                          PasswordEncoder passwordEncoder,
                          EstatisticasRepository estatisticasRepository,
                          AvaliacoesRepository avaliacoesRepository,
                          AnunciosRepository anunciosRepository){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.estatisticasRepository = estatisticasRepository;
        this.avaliacoesRepository = avaliacoesRepository;
        this.anunciosRepository = anunciosRepository;
    }

    public Cliente cadastrar(ClienteDTO clienteDTO){
        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.nome());
        cliente.setEmail(clienteDTO.email());
        cliente.setDataNascimento(clienteDTO.dataNascimento());
        cliente.setSenhaHash(passwordEncoder.encode(clienteDTO.senha()));
        cliente.setTelefone(clienteDTO.telefone());

        Cliente clienteSalvo = repository.save(cliente);

        Estatisticas stats = new Estatisticas();
        stats.setCliente(clienteSalvo);
        estatisticasRepository.save(stats);

        return clienteSalvo;
    }

    public List<Cliente> listar(){
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

    public Cliente atualizar(Long id, ClienteDTO clienteDTOAtualizado){

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setNome(clienteDTOAtualizado.nome());
        cliente.setEmail(clienteDTOAtualizado.email());
        cliente.setTelefone(clienteDTOAtualizado.telefone());
        cliente.setDataNascimento(clienteDTOAtualizado.dataNascimento());

        if (clienteDTOAtualizado.senha() != null && !clienteDTOAtualizado.senha().isBlank()) {
            cliente.setSenhaHash(passwordEncoder.encode(clienteDTOAtualizado.senha()));
        }

        return repository.save(cliente);

    }

    public void deletar(Long id){

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        cliente.setAtivo(false);

        repository.save(cliente);
    }

    public EstatisticasDTO buscarEstatisticas(Long clienteId){
        Estatisticas stats = estatisticasRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Estatísticas não encontradas para o cliente " + clienteId));

        long totalAvaliacoes = avaliacoesRepository.countByClienteId(clienteId);
        long totalAnuncios = anunciosRepository.countByClienteId(clienteId);

        return new EstatisticasDTO(
                (int) totalAvaliacoes,
                (int) totalAnuncios,
                stats.getTempoNoApp(),
                stats.getRankingPosicao(),
                stats.getPrecoMedio()
        );
    }







}
