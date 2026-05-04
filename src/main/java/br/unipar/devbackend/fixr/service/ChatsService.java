package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.ChatsRepository;
import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.Repository.MensagensRepository;
import br.unipar.devbackend.fixr.Repository.PrestadorRepository;
import br.unipar.devbackend.fixr.dto.ChatsDTO;
import br.unipar.devbackend.fixr.dto.MensagensDTO;
import br.unipar.devbackend.fixr.model.Chats;
import br.unipar.devbackend.fixr.model.Cliente;
import br.unipar.devbackend.fixr.model.Mensagens;
import br.unipar.devbackend.fixr.model.Prestador;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ChatsService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatsRepository chatsRepository;

    @Autowired
    private MensagensRepository mensagensRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrestadorRepository prestadorRepository;

    public Chats iniciarChamada(ChatsDTO dto) {
        System.out.println("iniciarChamada chamado por: " + dto.getChamadorNome());
        System.out.println("destinatario: " + dto.getDestinatarioId());
        Long clienteId   = dto.getPapelChamador() == Mensagens.PapelRemetente.CLIENTE
                ? dto.getChamadorId() : dto.getDestinatarioId();
        Long prestadorId = dto.getPapelChamador() == Mensagens.PapelRemetente.PRESTADOR
                ? dto.getChamadorId() : dto.getDestinatarioId();


        Chats chat = chatsRepository
                .findByClienteIdAndPrestadorId(clienteId, prestadorId)
                .orElseGet(() -> {
                    Chats novo = new Chats();
                    novo.setCliente(clienteRepository.getReferenceById(clienteId));
                    novo.setPrestador(prestadorRepository.getReferenceById(prestadorId));
                    novo.setStatus(Chats.StatusChat.PENDENTE);
                    return chatsRepository.save(novo);
                });


        String conteudo = dto.getPapelChamador() == Mensagens.PapelRemetente.CLIENTE
                ? dto.getChamadorNome() + " quer conversar com você"
                : dto.getChamadorNome() + " entrou em contato sobre: " + dto.getAnuncioTitulo();

        Mensagens msg = buildMensagem(chat, dto.getChamadorId(), dto.getChamadorNome(),
                dto.getPapelChamador(), conteudo, Mensagens.TipoMensagem.CALL_REQUEST);
        mensagensRepository.save(msg);

        System.out.println("Enviando notificação para usuário: " + dto.getDestinatarioId());
        messagingTemplate.convertAndSendToUser(
                String.valueOf(dto.getDestinatarioId()),
                "/queue/chamada",
                buildPayloadChamada(chat, msg, dto)
        );
        System.out.println("Notificação enviada!");

        return chat;
    }



    public Chats responderChamada(Long chatId, boolean aceitar, Long respondeuId) {
        Chats chat = chatsRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat não encontrado: " + chatId));

        if (aceitar) {
            chat.setStatus(Chats.StatusChat.ATIVO);
            chatsRepository.save(chat);

            Mensagens joinMsg = buildMensagemSistema(chat, "Chat iniciado", Mensagens.TipoMensagem.JOIN);
            mensagensRepository.save(joinMsg);


            messagingTemplate.convertAndSend("/topic/chat/" + chatId, joinMsg);


            Long chamadorId = respondeuId.equals(chat.getCliente().getId())
                    ? chat.getPrestador().getId() : chat.getCliente().getId();
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(chamadorId),
                    "/queue/resposta-chamada",
                    Map.of("chatId", chatId, "aceito", true)
            );
        } else {
            chat.setStatus(Chats.StatusChat.ENCERRADO);
            chat.setDataEncerramento(LocalDateTime.now());
            chatsRepository.save(chat);

            Long chamadorId = respondeuId.equals(chat.getCliente().getId())
                    ? chat.getPrestador().getId() : chat.getCliente().getId();
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(chamadorId),
                    "/queue/resposta-chamada",
                    Map.of("chatId", chatId, "aceito", false)
            );
        }

        return chat;
    }



    public Mensagens enviarMensagem(MensagensDTO dto) {
        Chats chat = chatsRepository.findById(dto.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat não encontrado: " + dto.getChatId()));

        if (chat.getStatus() != Chats.StatusChat.ATIVO) {
            throw new IllegalStateException("Chat não está ativo");
        }

        Mensagens msg = new Mensagens();
        msg.setChat(chat);
        msg.setTexto(dto.getTexto());
        msg.setTipo(dto.getTipo());
        msg.setPapelRemetente(dto.getPapelRemetente());


        if (dto.getPapelRemetente() == Mensagens.PapelRemetente.CLIENTE) {
            msg.setRemetente(clienteRepository.getReferenceById(dto.getRemetenteId()));
        } else {
            msg.setRemetente(prestadorRepository.getReferenceById(dto.getRemetenteId()));
        }

        Mensagens salva = mensagensRepository.save(msg);


        messagingTemplate.convertAndSend("/topic/chat/" + dto.getChatId(), salva);

        return salva;
    }



    public void encerrarChat(Long chatId) {
        Chats chat = chatsRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat não encontrado: " + chatId));

        chat.setStatus(Chats.StatusChat.ENCERRADO);
        chat.setDataEncerramento(LocalDateTime.now());
        chatsRepository.save(chat);

        Mensagens leaveMsg = buildMensagemSistema(chat, "Chat encerrado", Mensagens.TipoMensagem.LEAVE);
        mensagensRepository.save(leaveMsg);

        messagingTemplate.convertAndSend("/topic/chat/" + chatId, leaveMsg);
    }



    @Transactional(readOnly = true)
    public List<Mensagens> buscarHistorico(Long chatId) {
        return mensagensRepository.findByChatIdOrderByEnviadoEmAsc(chatId);
    }



    private Mensagens buildMensagem(Chats chat, Long remetenteId, String remetenteNome,
                                    Mensagens.PapelRemetente papel, String texto, Mensagens.TipoMensagem tipo) {
        Mensagens msg = new Mensagens();
        msg.setChat(chat);
        msg.setTexto(texto);
        msg.setTipo(tipo);
        msg.setPapelRemetente(papel);
        if (papel == Mensagens.PapelRemetente.CLIENTE) {
            msg.setRemetente(clienteRepository.getReferenceById(remetenteId));
        } else {
            msg.setRemetente(prestadorRepository.getReferenceById(remetenteId));
        }
        return msg;
    }



    private Mensagens buildMensagemSistema(Chats chat, String texto, Mensagens.TipoMensagem tipo) {
        Mensagens msg = new Mensagens();
        msg.setChat(chat);
        msg.setTexto(texto);
        msg.setTipo(tipo);
        msg.setPapelRemetente(Mensagens.PapelRemetente.CLIENTE);
        return msg;
    }



    private Map<String, Object> buildPayloadChamada(Chats chat, Mensagens msg, ChatsDTO dto) {
        return Map.of(
                "chatId",        chat.getId(),
                "chamadorId",    dto.getChamadorId(),
                "chamadorNome",  dto.getChamadorNome(),
                "papelChamador", dto.getPapelChamador().name(),
                "anuncioTitulo", dto.getAnuncioTitulo() != null ? dto.getAnuncioTitulo() : "",
                "mensagem",      msg.getTexto()
        );
    }

    public Chats cadastrar(ChatsDTO dto) {
        Chats chat = new Chats();
        chat.setCliente(clienteRepository.getReferenceById(dto.getClienteId()));
        chat.setPrestador(prestadorRepository.getReferenceById(dto.getPrestadorId()));
        chat.setStatus(Chats.StatusChat.PENDENTE);
        return chatsRepository.save(chat);
    }

    public List<Chats> listar() {
        return chatsRepository.findAll();
    }

    public Chats buscarPorId(Long id) {
        return chatsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chat não encontrado: " + id));
    }

    public Chats atualizar(Long id, ChatsDTO dto) {
        Chats chat = buscarPorId(id);
        chat.setCliente(clienteRepository.getReferenceById(dto.getClienteId()));
        chat.setPrestador(prestadorRepository.getReferenceById(dto.getPrestadorId()));
        return chatsRepository.save(chat);
    }

    public void deletar(Long id) {
        Chats chat = buscarPorId(id);
        chat.setAtivo(false);
        chatsRepository.save(chat);
    }

}
