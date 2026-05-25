package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.*;
import br.unipar.devbackend.fixr.dto.ChatsDTO;
import br.unipar.devbackend.fixr.dto.MensagensDTO;
import br.unipar.devbackend.fixr.model.Chats;
import br.unipar.devbackend.fixr.model.Mensagens;
import br.unipar.devbackend.fixr.model.Usuario;
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

    @Autowired
    private AnunciosRepository anunciosRepository;

    // ─── CRUD básico ──────────────────────────────────────────────────────

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

    // ─── Iniciar chamada ──────────────────────────────────────────────────

    public Chats iniciarChamada(ChatsDTO dto) {
//        System.out.println("anuncioId recebido: " + dto.getAnuncioId());

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

                    return novo;
                });

        if (dto.getAnuncioId() != null) {
            chat.setAnuncio(anunciosRepository.getReferenceById(dto.getAnuncioId()));
        }

        chatsRepository.save(chat);

        String conteudo = dto.getPapelChamador() == Mensagens.PapelRemetente.CLIENTE
                ? dto.getChamadorNome() + " quer conversar com você"
                : dto.getChamadorNome() + " entrou em contato sobre: " + dto.getAnuncioTitulo();

        Mensagens msg = buildMensagem(chat, dto.getChamadorId(),
                dto.getPapelChamador(), conteudo, Mensagens.TipoMensagem.CALL_REQUEST);
        mensagensRepository.save(msg);

//        System.out.println("Enviando notificação para tópico: /topic/usuario/" + dto.getDestinatarioId() + "/chamada");

        messagingTemplate.convertAndSend(
                "/topic/usuario/" + dto.getDestinatarioId() + "/chamada",(Object)
                buildPayloadChamada(chat, msg, dto)
        );

//        System.out.println("Notificação enviada!");

        return chat;
    }

    // ─── Responder chamada ────────────────────────────────────────────────

    public Chats responderChamada(Long chatId, boolean aceitar, Long respondeuId) {
        Chats chat = chatsRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat não encontrado: " + chatId));

        Long chamadorId = respondeuId.equals(chat.getCliente().getId())
                ? chat.getPrestador().getId()
                : chat.getCliente().getId();

//        System.out.println("respondeuId: " + respondeuId);
//        System.out.println("clienteId: " + chat.getCliente().getId());
//        System.out.println("prestadorId: " + chat.getPrestador().getId());
//        System.out.println("chamadorId calculado: " + chamadorId);
//        System.out.println("enviando resposta para: /topic/usuario/" + chamadorId + "/resposta-chamada");

        if (aceitar) {
            chat.setStatus(Chats.StatusChat.ATIVO);
            chatsRepository.save(chat);

            Mensagens joinMsg = buildMensagemSistema(chat, "Chat iniciado", Mensagens.TipoMensagem.JOIN);
            mensagensRepository.save(joinMsg);

            messagingTemplate.convertAndSend("/topic/chat/" + chatId, joinMsg);

            messagingTemplate.convertAndSend(
                    "/topic/usuario/" + chamadorId + "/resposta-chamada",(Object)
                    Map.of("chatId", chatId, "aceito", true)
            );
        } else {
            chat.setStatus(Chats.StatusChat.ENCERRADO);
            chat.setDataEncerramento(LocalDateTime.now());
            chatsRepository.save(chat);

            messagingTemplate.convertAndSend(
                    "/topic/usuario/" + chamadorId + "/resposta-chamada",(Object)
                    Map.of("chatId", chatId, "aceito", false)
            );
        }

        return chat;
    }

    // ─── Enviar mensagem ──────────────────────────────────────────────────

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
        msg.setRemetente(resolverRemetente(dto.getRemetenteId(), dto.getPapelRemetente()));

        Mensagens salva = mensagensRepository.save(msg);

        messagingTemplate.convertAndSend("/topic/chat/" + dto.getChatId(), salva);

        return salva;
    }

    // ─── Encerrar chat ────────────────────────────────────────────────────

    public void encerrarChat(Long chatId) {
//        System.out.println("encerrarChat chamado: " + chatId);
        Chats chat = chatsRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat não encontrado: " + chatId));

        chat.setStatus(Chats.StatusChat.ENCERRADO);
        chat.setDataEncerramento(LocalDateTime.now());
        chatsRepository.save(chat);

        Mensagens leaveMsg = buildMensagemSistema(chat, "Chat encerrado", Mensagens.TipoMensagem.LEAVE);
        mensagensRepository.save(leaveMsg);

        messagingTemplate.convertAndSend("/topic/chat/" + chatId, leaveMsg);
    }

    // ─── Histórico ────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Mensagens> buscarHistorico(Long chatId) {
        return mensagensRepository.findByChatIdOrderByEnviadoEmAsc(chatId);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────

    private Usuario resolverRemetente(Long id, Mensagens.PapelRemetente papel) {
        if (papel == Mensagens.PapelRemetente.CLIENTE) {
            return clienteRepository.getReferenceById(id);
        }
        return prestadorRepository.getReferenceById(id);
    }

    private Mensagens buildMensagem(Chats chat, Long remetenteId,
                                    Mensagens.PapelRemetente papel,
                                    String texto, Mensagens.TipoMensagem tipo) {
        Mensagens msg = new Mensagens();
        msg.setChat(chat);
        msg.setTexto(texto);
        msg.setTipo(tipo);
        msg.setPapelRemetente(papel);
        msg.setRemetente(resolverRemetente(remetenteId, papel));
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

    @Transactional(readOnly = true)
    public List<Chats> listarPorCliente(Long clienteId) {
        return chatsRepository.findByClienteIdAndStatus(clienteId, Chats.StatusChat.ATIVO);
    }

    @Transactional(readOnly = true)
    public List<Chats> listarPorPrestador(Long prestadorId) {
        return chatsRepository.findByPrestadorIdAndStatus(prestadorId, Chats.StatusChat.ATIVO);
    }
}