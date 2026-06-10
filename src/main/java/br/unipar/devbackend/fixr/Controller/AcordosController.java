package br.unipar.devbackend.fixr.Controller;


import br.unipar.devbackend.fixr.dto.AcordosDTO;
import br.unipar.devbackend.fixr.model.Acordos;
import br.unipar.devbackend.fixr.service.AcordosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    public AcordosController(AcordosService acordosService) {
        this.acordosService = acordosService;
    }

    @PostMapping
    public Acordos cadastrar(@RequestBody @Valid AcordosDTO acordosDTO) {
        return acordosService.cadastrar(acordosDTO);
    }

    @GetMapping
    public List<Acordos> listar() {
        return acordosService.listar();
    }

    @GetMapping("/{id}")
    public Acordos buscarPorId(@PathVariable Long id) {
        return acordosService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Acordos atualizar(@PathVariable Long id, @Valid @RequestBody AcordosDTO acordosDTO) {
        return acordosService.atualizar(id, acordosDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        acordosService.deletar(id);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> buscarAcordoPorChat(@PathVariable Long chatId) {
        return acordosService.buscarPorChatId(chatId)
                .map(acordo -> {
                    Map<String, Object> dto = Map.of(
                            "id", acordo.getId(),
                            "valor", acordo.getValor() != null ? acordo.getValor() : 0,
                            "valor2", acordo.getValor2() != null ? acordo.getValor2() : 0,
                            "status", acordo.getStatusAcordo().name(),
                            "chatId", acordo.getChats().getId()
                    );
                    return ResponseEntity.ok((Object) dto);
                })
                .orElse(ResponseEntity.noContent().build());
    }
}
