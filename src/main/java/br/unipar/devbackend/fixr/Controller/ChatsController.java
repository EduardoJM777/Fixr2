package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.ChatsDTO;
import br.unipar.devbackend.fixr.dto.MensagensDTO;
import br.unipar.devbackend.fixr.model.Chats;
import br.unipar.devbackend.fixr.model.Mensagens;
import br.unipar.devbackend.fixr.service.ChatsService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/chats")
@CrossOrigin(origins = "*")
public class ChatsController {

    private final ChatsService chatsService;

    public ChatsController(ChatsService chatsService) {
        this.chatsService = chatsService;
    }


    @PostMapping
    public Chats cadastrar(@Valid @RequestBody ChatsDTO chatsDTO) {
        return chatsService.cadastrar(chatsDTO);
    }

    @GetMapping
    public List<Chats> listar() {
        return chatsService.listar();
    }

    @GetMapping("/{id}")
    public Chats buscarPorId(@PathVariable Long id) {
        return chatsService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Chats atualizar(@PathVariable Long id, @Valid @RequestBody ChatsDTO chatsDTO) {
        return chatsService.atualizar(id, chatsDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        chatsService.deletar(id);
    }



    @PostMapping("/chamar")
    public Chats iniciarChamada(@RequestBody ChatsDTO dto) {
        return chatsService.iniciarChamada(dto);
    }

    @PostMapping("/responder")
    public Chats responderChamada(@RequestBody Map<String, Object> body) {
        Long chatId   = (Long) body.get("chatId");
        boolean aceitar  = Boolean.TRUE.equals(body.get("aceitar"));
        Long respondeuId = Long.valueOf(body.get("respondeuId").toString());
        return chatsService.responderChamada(chatId, aceitar, respondeuId);
    }

    @GetMapping("/historico/{chatId}")
    public List<Mensagens> buscarHistorico(@PathVariable Long chatId) {
        return chatsService.buscarHistorico(chatId);
    }

    @PostMapping("/encerrar/{chatId}")
    public void encerrarChat(@PathVariable Long chatId) {
        chatsService.encerrarChat(chatId);
    }



    @MessageMapping("/chat.enviar")
    public void enviarMensagem(@Payload MensagensDTO dto) {
        chatsService.enviarMensagem(dto);
    }

    @MessageMapping("/chat.chamar")
    public void iniciarChamadaWs(@Payload ChatsDTO dto) {
        chatsService.iniciarChamada(dto);
    }

    @MessageMapping("/chat.responder")
    public void responderChamadaWs(@Payload Map<String, Object> payload) {
        Long chatId   = Long.valueOf(payload.get("chatId").toString());
        boolean aceitar  = Boolean.TRUE.equals(payload.get("aceitar"));
        Long respondeuId = Long.valueOf(payload.get("respondeuId").toString());
        chatsService.responderChamada(chatId, aceitar, respondeuId);
    }

    @MessageMapping("/chat.encerrar")
    public void encerrarChatWs(@Payload Map<String, Object> payload) {
        Long chatId = Long.valueOf(payload.get("chatId").toString());
        chatsService.encerrarChat(chatId);
    }
}