package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.dto.ClienteDTO;
import br.unipar.devbackend.fixr.model.Cliente;
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

    public Cliente salvar(ClienteDTO clienteDTO){
        Cliente cliente = new Cliente();
        cliente.setNome(cliente.getNome());
        cliente.setEmail(cliente.getEmail());
        return repository.save(cliente);
    }

    public List<Cliente> listar(){
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado){
        return repository.findById(id).map(cliente -> {
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setEmail(clienteAtualizado.getEmail());
            cliente.setSenhaHash(clienteAtualizado.getSenhaHash());
            return repository.save(cliente);
        }).orElseThrow(() -> new RuntimeException("Erro"));
    }

    public void apagar(Long id){
        repository.deleteById(id);
    }

}
