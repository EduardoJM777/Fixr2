package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.PrestadorDTO;
import br.unipar.devbackend.fixr.model.Prestador;
import br.unipar.devbackend.fixr.service.PrestadorService;
import jakarta.validation.Valid;
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
    public Prestador cadastrar(@Valid @RequestBody PrestadorDTO prestadorDTO){
        return prestadorService.cadastrar(prestadorDTO);
    }

    @PutMapping("/{id}")
    public Prestador atualizar(@PathVariable Long id, @Valid @RequestBody PrestadorDTO prestadorDTO){
        return prestadorService.atualizar(id, prestadorDTO);
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

}
