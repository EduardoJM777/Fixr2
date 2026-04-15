package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.MensagensDTO;
import br.unipar.devbackend.fixr.model.Mensagens;
import br.unipar.devbackend.fixr.service.MensagensService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mensagens")
@CrossOrigin(origins = "*")
public class MensagensController {

    private final MensagensService mensagensService;

    public MensagensController(MensagensService service){
        this.mensagensService = service;
    }

    @PostMapping
    public Mensagens cadastrar(@Valid @RequestBody MensagensDTO mensagensDTO){
        return mensagensService.cadastrar(mensagensDTO);
    }

    @GetMapping
    public List<Mensagens> listar(){
        return mensagensService.listar();
    }

    @GetMapping("/{id}")
    public Mensagens buscarPorId(@PathVariable Long id){
        return mensagensService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Mensagens atualizar(@PathVariable Long id, @Valid @RequestBody MensagensDTO mensagensDTO){
        return mensagensService.atualizar(id, mensagensDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        mensagensService.deletar(id);
    }


}
