package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.ChatsRepository;
import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.Repository.PrestadorRepository;
import br.unipar.devbackend.fixr.dto.ChatsDTO;
import br.unipar.devbackend.fixr.model.Chats;
import br.unipar.devbackend.fixr.model.Cliente;
import br.unipar.devbackend.fixr.model.Prestador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatsService {

    private final ChatsRepository repository;
    private final PrestadorRepository prestadorRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public ChatsService(ChatsRepository chatsRepository, PrestadorRepository prestadorRepository, ClienteRepository clienteRepository){
        this.repository = chatsRepository;
        this.prestadorRepository = prestadorRepository;
        this.clienteRepository = clienteRepository;
    }

    public Chats cadastrar(ChatsDTO chatsDTO){
        Chats chats = new Chats();

        Prestador prestador = prestadorRepository.getReferenceById(chatsDTO.idPrestador());
        chats.setPrestador(prestador);

        Cliente cliente = clienteRepository.getReferenceById(chatsDTO.idCliente());
        chats.setCliente(cliente);

        return repository.save(chats);
    }

    public List<Chats> listar(){
        return repository.findAll();
    }

    public Chats buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Chat não encontrado"));
    }

    public void deletar(Long id){
        repository.deleteById(id);
    }

    public Chats atualizar(Long id, ChatsDTO chatsDTOatualizado){
        return repository.findById(id).map(chats -> {

            Prestador prestador = prestadorRepository.getReferenceById(chatsDTOatualizado.idPrestador());
            chats.setPrestador(prestador);

            Cliente cliente = clienteRepository.getReferenceById(chatsDTOatualizado.idCliente());
            chats.setCliente(cliente);

            return repository.save(chats);
        }).orElseThrow(() -> new RuntimeException("Não encontrado."));
    }

}
