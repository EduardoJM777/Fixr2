package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.MensagensRepository;
import br.unipar.devbackend.fixr.model.Mensagens;

import java.util.List;

public class MensagensService {
    private final MensagensRepository repository;

    public MensagensService(MensagensRepository repository) {
        this.repository = repository;
    }

        public Mensagens criar(Mensagens mensagens){
            return repository.save(mensagens);
    }

    public List<Mensagens> listar(){
        return repository.findAll();
    }

    public Mensagens listarPorId(Long id){
        return repository.findById(id).orElseThrow(()->new RuntimeException("Não encontrado"));
    }

    public void deletar(Mensagens mensagens){}

    public Mensagens atualizar(Long id, Mensagens mensagensatualizadas){
        return repository.findById(id).map(Mensagens ->{
            mensagensatualizadas.setChat(mensagensatualizadas.getChat());
            mensagensatualizadas.setTexto(mensagensatualizadas.getTexto());
            mensagensatualizadas.setEnviadoEm(mensagensatualizadas.getEnviadoEm());
            return repository.save(mensagensatualizadas);
        }).orElseThrow(()->new RuntimeException("Não encontrado"));
    }

}
