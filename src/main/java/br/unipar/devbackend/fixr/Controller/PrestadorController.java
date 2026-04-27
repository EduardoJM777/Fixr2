package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.EstatisticasPrestadorDTO;
import br.unipar.devbackend.fixr.dto.PrestadorDTO;
import br.unipar.devbackend.fixr.model.Prestador;
import br.unipar.devbackend.fixr.service.PrestadorService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prestador")
@CrossOrigin(origins = "*")
public class PrestadorController {

    private final PrestadorService prestadorService;

    public PrestadorController(PrestadorService service){
        this.prestadorService = service;
    }

    @PostMapping
    public Prestador cadastrar(@Valid @RequestBody PrestadorDTO dto){
        return prestadorService.cadastrar(dto);
    }

    @PutMapping("/{id}")
    public Prestador atualizar(@PathVariable Long id, @Valid @RequestBody PrestadorDTO dto){
        return prestadorService.atualizar(id, dto);
    }

    @GetMapping
    public List<Prestador> listar(){
        return prestadorService.listar();
    }

    @GetMapping("/{id}")
    public Prestador buscarPorId(@PathVariable Long id){
        return prestadorService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        prestadorService.deletar(id);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<EstatisticasPrestadorDTO> getEstatisticas(@PathVariable Long id) {
        return ResponseEntity.ok(prestadorService.buscarEstatisticas(id));
    }

    @PatchMapping("/{id}/stats/experiencia")
    public ResponseEntity<Void> atualizarExperiencia(@PathVariable Long id,
                                                     @RequestBody Map<String, String> body){
        prestadorService.atualizarExperiencia(id, body.get("experienciaTrabalho"));
        return ResponseEntity.noContent().build();
    }

}