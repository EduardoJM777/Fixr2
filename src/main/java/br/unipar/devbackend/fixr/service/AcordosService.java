package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.AcordosRepository;
import br.unipar.devbackend.fixr.Repository.ChatsRepository;
import br.unipar.devbackend.fixr.dto.AcordosDTO;
import br.unipar.devbackend.fixr.model.Acordos;
import br.unipar.devbackend.fixr.model.Chats;
import br.unipar.devbackend.fixr.model.StatusAcordo;
import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcordosService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
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

    public void iniciarAcordo(Long chatId, Double valor, Long iniciadorId,
                              String iniciadorNome, String papel) {
        Chats chat = chatsRepository.getReferenceById(chatId);

        // cancela acordo anterior se existir
        repository.findTopByChatsIdOrderByIdDesc(chatId).ifPresent(a -> {
            if (a.getStatusAcordo() == StatusAcordo.ATIVO) {
                a.setStatusAcordo(StatusAcordo.CANCELADO);
                repository.save(a);
            }
        });

        Acordos acordo = new Acordos();
        acordo.setChats(chat);
        acordo.setValor(valor);
        acordo.setStatusAcordo(StatusAcordo.ATIVO);
        repository.save(acordo);

        // descobre o destinatário
        Long destinatarioId = papel.equals("CLIENTE")
                ? chat.getPrestador().getId()
                : chat.getCliente().getId();

        messagingTemplate.convertAndSend(
                "/topic/usuario/" + destinatarioId + "/acordo",(Object)
                Map.of(
                        "tipo", "NOVA_PROPOSTA",
                        "acordoId", acordo.getId(),
                        "chatId", chatId,
                        "valor", valor,
                        "iniciadorNome", iniciadorNome,
                        "papel", papel
                )
        );

        // notifica o próprio iniciador (confirmação)
        messagingTemplate.convertAndSend(
                "/topic/usuario/" + iniciadorId + "/acordo", (Object)
                Map.of(
                        "tipo", "PROPOSTA_ENVIADA",
                        "acordoId", acordo.getId(),
                        "chatId", chatId,
                        "valor", valor
                )
        );
    }

    public void contraproposta(Long acordoId, Double valor, Long usuarioId) {
        Acordos acordo = repository.findById(acordoId)
                .orElseThrow(() -> new RuntimeException("Acordo não encontrado"));

        acordo.setValor2(valor);
        repository.save(acordo);

        Long clienteId = acordo.getChats().getCliente().getId();
        Long prestadorId = acordo.getChats().getPrestador().getId();
        Long destinatarioId = usuarioId.equals(clienteId) ? prestadorId : clienteId;

        messagingTemplate.convertAndSend(
                "/topic/usuario/" + destinatarioId + "/acordo", (Object)
                Map.of(
                        "tipo", "CONTRAPROPOSTA",
                        "acordoId", acordoId,
                        "valor", valor
                )
        );
    }

    public void aceitarAcordo(Long acordoId, Long usuarioId) {
        Acordos acordo = repository.findById(acordoId)
                .orElseThrow(() -> new RuntimeException("Acordo não encontrado"));

        acordo.setStatusAcordo(StatusAcordo.FINALIZADO);
        repository.save(acordo);

        Long clienteId = acordo.getChats().getCliente().getId();
        Long prestadorId = acordo.getChats().getPrestador().getId();
        Long outroId = usuarioId.equals(clienteId) ? prestadorId : clienteId;

        Map<String, Object> payload = Map.of(
                "tipo", "ACORDO_ACEITO",
                "acordoId", acordoId,
                "valorFinal", acordo.getValor2() != null ? acordo.getValor2() : acordo.getValor()
        );

        messagingTemplate.convertAndSend("/topic/usuario/" + usuarioId + "/acordo", (Object) payload);
        messagingTemplate.convertAndSend("/topic/usuario/" + outroId + "/acordo", (Object) payload);
    }

    public void cancelarAcordo(Long acordoId) {
        Acordos acordo = repository.findById(acordoId)
                .orElseThrow(() -> new RuntimeException("Acordo não encontrado"));

        acordo.setStatusAcordo(StatusAcordo.CANCELADO);
        repository.save(acordo);

        Long clienteId = acordo.getChats().getCliente().getId();
        Long prestadorId = acordo.getChats().getPrestador().getId();

        Map<String, Object> payload = Map.of("tipo", "ACORDO_CANCELADO", "acordoId", acordoId);
        messagingTemplate.convertAndSend("/topic/usuario/" + clienteId + "/acordo", (Object) payload);
        messagingTemplate.convertAndSend("/topic/usuario/" + prestadorId + "/acordo", (Object) payload);
    }

    public Optional<Acordos> buscarPorChatId(Long chatId) {
        return repository.findTopByChatsIdOrderByIdDesc(chatId);
    }

    public List<Acordos> listar(){ return repository.findAll();}

    public Acordos buscarPorId(Long id){return repository.findById(id).
            orElseThrow(()->new RuntimeException("Não encontrado"));}

    public void deletar(Long id){
        Acordos acordos = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Acordo não encontrado"));

        acordos.setAtivo(false);

        repository.save(acordos);
    }

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
