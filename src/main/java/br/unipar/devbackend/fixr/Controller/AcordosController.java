package br.unipar.devbackend.fixr.Controller;


import br.unipar.devbackend.fixr.dto.AcordosDTO;
import br.unipar.devbackend.fixr.model.Acordos;
import br.unipar.devbackend.fixr.service.AcordosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/acordos")
@CrossOrigin(origins="*")
public class AcordosController {
    private final AcordosService acordosService;
    public AcordosController(AcordosService acordosService) {this.acordosService = acordosService;}

    @PostMapping
    public Acordos cadastrar(@RequestBody @Valid AcordosDTO acordosDTO){
        return acordosService.cadastrar(acordosDTO);
    }

    @GetMapping
    public List<Acordos> listar(){
        return acordosService.listar();
    }

    @GetMapping("/{id}")
    public Acordos buscarPorId(@PathVariable Long id){
        return acordosService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Acordos atualizar(@PathVariable Long id, @Valid @RequestBody AcordosDTO acordosDTO) {
        return acordosService.atualizar(id, acordosDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        acordosService.deletar(id);}

    @MessageMapping("/acordo.iniciar")
    public void iniciarAcordoWs(@Payload Map<String, Object> payload) {
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

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<Acordos> buscarAcordoPorChat(@PathVariable Long chatId) {
        return acordosService.buscarPorChatId(chatId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

}
