package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.AvaliacoesDTO;
import br.unipar.devbackend.fixr.model.Avaliacoes;
import br.unipar.devbackend.fixr.service.AvaliacoesService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@CrossOrigin(origins = "*")
public class AvaliacoesController {

    private final AvaliacoesService service;

    public AvaliacoesController(AvaliacoesService service){
        this.service = service;
    }

    @PostMapping
    public Avaliacoes cadastrar(@Valid @RequestBody AvaliacoesDTO avaliacoesDTO){
        return service.cadastrar(avaliacoesDTO);
    }

    @GetMapping
    public List<Avaliacoes> listar(){
        return service.listar();
    }

    @GetMapping("/{id}")
    public Avaliacoes buscarPorId(@PathVariable Long id){
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Avaliacoes atualizar(@PathVariable Long id, @Valid @RequestBody AvaliacoesDTO avaliacoesDTO){
        return service.atualizar(id, avaliacoesDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        service.deletar(id);
    }

}
