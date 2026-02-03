package br.unipar.devbackend.fixr.Controller;


import br.unipar.devbackend.fixr.dto.AcordosDTO;
import br.unipar.devbackend.fixr.model.Acordos;
import br.unipar.devbackend.fixr.service.AcordosService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Acordos atualizar(@PathVariable Long id, @RequestBody AcordosDTO acordosDTO) {
        return acordosService.atualizar(id, acordosDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        acordosService.deletar(id);}

}
