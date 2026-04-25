package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.EstatisticasPrestadorDTO;
import br.unipar.devbackend.fixr.dto.PrestadorDTO;
import br.unipar.devbackend.fixr.model.Prestador;
import br.unipar.devbackend.fixr.service.PrestadorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<EstatisticasPrestadorDTO> getEstatisticas(@PathVariable Integer id) {
        return ResponseEntity.ok(prestadorService.buscarEstatisticas(id));
    }

}