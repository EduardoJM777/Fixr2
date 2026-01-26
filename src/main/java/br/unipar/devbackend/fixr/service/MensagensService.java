package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.ChatsRepository;
import br.unipar.devbackend.fixr.Repository.MensagensRepository;
import br.unipar.devbackend.fixr.dto.MensagensDTO;
import br.unipar.devbackend.fixr.model.Chats;
import br.unipar.devbackend.fixr.model.Mensagens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensagensService {

    private final MensagensRepository repository;
    private final ChatsRepository chatsRepository;

    @Autowired
    public MensagensService(MensagensRepository repository, ChatsRepository chatsRepository) {
        this.repository = repository;
        this.chatsRepository = chatsRepository;
    }

    public Mensagens criar(MensagensDTO mensagensDTO){
        Mensagens mensagens = new Mensagens();
        mensagens.setTexto(mensagensDTO.texto());

        Chats chats = chatsRepository.getReferenceById(mensagensDTO.idChat());
        mensagens.setChat(chats);

        return repository.save(mensagens);
    }

    public List<Mensagens> listar(){
        return repository.findAll();
    }

    public Mensagens listarPorId(Long id){
        return repository.findById(id).orElseThrow(()->new RuntimeException("Mensagem não encontrada"));
    }

    public void deletar(Long id){
        repository.deleteById(id);
    }

    public Mensagens atualizar(Long id, MensagensDTO mensagensDTOatualizadas){
        return repository.findById(id).map(mensagens ->{
            mensagens.setTexto(mensagensDTOatualizadas.texto());

            Chats chats = chatsRepository.getReferenceById(mensagensDTOatualizadas.idChat());
            mensagens.setChat(chats);

            return repository.save(mensagens);
        }).orElseThrow(()->new RuntimeException("Não encontrado"));
    }

}
