package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.ChatsDTO;
import br.unipar.devbackend.fixr.dto.MensagensDTO;
import br.unipar.devbackend.fixr.model.Chats;
import br.unipar.devbackend.fixr.model.Mensagens;
import br.unipar.devbackend.fixr.service.AcordosService;
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
    private final AcordosService acordosService;

    public ChatsController(ChatsService chatsService, AcordosService acordosService) {
        this.chatsService = chatsService;
        this.acordosService = acordosService;
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

    @GetMapping("/cliente/{clienteId}")
    public List<Chats> listarPorCliente(@PathVariable Long clienteId) {
        return chatsService.listarPorCliente(clienteId);
    }

    @GetMapping("/prestador/{prestadorId}")
    public List<Chats> listarPorPrestador(@PathVariable Long prestadorId) {
        return chatsService.listarPorPrestador(prestadorId);
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

    // Adiciona no ChatsController.java, junto aos outros @MessageMapping:

    @MessageMapping("/acordo.iniciar")
    public void iniciarAcordoWs(@Payload Map<String, Object> payload) {
        System.out.println("ACORDO INICIAR RECEBIDO: " + payload);
        Long chatId = Long.valueOf(payload.get("chatId").toString());
        Double valor = Double.valueOf(payload.get("valor").toString());
        Long iniciadorId = Long.valueOf(payload.get("iniciadorId").toString());
        String iniciadorNome = payload.get("iniciadorNome").toString();
        String papel = payload.get("papel").toString();
        acordosService.iniciarAcordo(chatId, valor, iniciadorId, iniciadorNome, papel);
    }

    @MessageMapping("/acordo.contraproposta")
    public void contraPropostaWs(@Payload Map<String, Object> payload) {
        Long acordoId = Long.valueOf(payload.get("acordoId").toString());
        Double valor = Double.valueOf(payload.get("valor").toString());
        Long usuarioId = Long.valueOf(payload.get("usuarioId").toString());
        acordosService.contraproposta(acordoId, valor, usuarioId);
    }

    @MessageMapping("/acordo.aceitar")
    public void aceitarAcordoWs(@Payload Map<String, Object> payload) {
        Long acordoId = Long.valueOf(payload.get("acordoId").toString());
        Long usuarioId = Long.valueOf(payload.get("usuarioId").toString());
        acordosService.aceitarAcordo(acordoId, usuarioId);
    }

    @MessageMapping("/acordo.cancelar")
    public void cancelarAcordoWs(@Payload Map<String, Object> payload) {
        Long acordoId = Long.valueOf(payload.get("acordoId").toString());
        acordosService.cancelarAcordo(acordoId);
    }
}