package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.dto.ClienteDTO;
import br.unipar.devbackend.fixr.model.Cliente;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    @Autowired
    public ClienteService(ClienteRepository repository){
        this.repository = repository;
    }

    public Cliente cadastrar(ClienteDTO clienteDTO){
        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.nome());
        cliente.setEmail(clienteDTO.email());
        cliente.setDataNascimento(clienteDTO.dataNascimento());
        cliente.setSenhaHash(clienteDTO.senha());
        cliente.setTelefone(clienteDTO.telefone());
        return repository.save(cliente);
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
            cliente.setSenhaHash(clienteDTOAtualizado.senha());
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

}
