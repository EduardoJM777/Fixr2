package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.AcordosRepository;
import br.unipar.devbackend.fixr.Repository.ChatsRepository;
import br.unipar.devbackend.fixr.dto.AcordosDTO;
import br.unipar.devbackend.fixr.model.Acordos;
import br.unipar.devbackend.fixr.model.Chats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcordosService {
    private final AcordosRepository repository;
    private final ChatsRepository chatsRepository;

    @Autowired
    public AcordosService(AcordosRepository repository,  ChatsRepository chatsRepository) {
        this.repository = repository;
        this.chatsRepository = chatsRepository;
    }

    public Acordos cadastrar(AcordosDTO acordosDTO){
        Acordos acordos = new Acordos();
        acordos.setValor(acordosDTO.valor());
        acordos.setValor2(acordosDTO.valor2());
        acordos.setStatusAcordo(acordosDTO.statusAcordo());

        Chats chats = chatsRepository.getReferenceById(acordosDTO.idChats());
        acordos.setChats(chats);

        return repository.save(acordos);
    }

    public List<Acordos> listar(){ return repository.findAll();}

    public Acordos buscarPorId(Long id){return repository.findById(id).
            orElseThrow(()->new RuntimeException("Não encontrado"));}

    public void deletar(Long id){repository.deleteById(id);}

    public Acordos atualizar(Long id, AcordosDTO acordosDTOAtualizado) {
        return repository.findById(id).map(acordos -> {
            Chats chats = chatsRepository.getReferenceById(acordosDTOAtualizado.idChats());
            acordos.setChats(chats);

            acordos.setData_servico(acordosDTOAtualizado.data_servico());
            acordos.setValor(acordosDTOAtualizado.valor());
            acordos.setValor2(acordosDTOAtualizado.valor2());
            acordos.setStatusAcordo(acordosDTOAtualizado.statusAcordo());
            return repository.save(acordos);
        }).orElseThrow(() -> new RuntimeException("Não encontrado"));
    };
}
