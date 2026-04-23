package br.unipar.devbackend.fixr.service;

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

    @Autowired
    public ClienteService(ClienteRepository repository,
                          PasswordEncoder passwordEncoder,
                          EstatisticasRepository estatisticasRepository){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.estatisticasRepository = estatisticasRepository;
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
        return repository.findById(id).map(cliente -> {
            cliente.setNome(clienteDTOAtualizado.nome());
            cliente.setEmail(clienteDTOAtualizado.email());
            cliente.setDataNascimento(clienteDTOAtualizado.dataNascimento());
            cliente.setSenhaHash(passwordEncoder.encode(clienteDTOAtualizado.senha()));
            cliente.setTelefone(clienteDTOAtualizado.telefone());
            return repository.save(cliente);
        }).orElseThrow(() -> new RuntimeException("Erro"));
    }

    public void deletar(Long id){

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        cliente.setAtivo(false);

        repository.save(cliente);
    }

    public EstatisticasDTO buscarEstatisticas(Integer clienteId){
        Estatisticas stats = estatisticasRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Estatísticas não encontradas para o cliente " + clienteId));

        return new EstatisticasDTO(
                stats.getAvaliacoesRecebidas(),
                stats.getAnunciosPublicados(),
                stats.getTempoNoApp(),
                stats.getRankingPosicao(),
                stats.getPrecoMedio()
        );
    }







}
