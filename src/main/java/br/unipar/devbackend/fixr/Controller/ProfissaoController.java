package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.ProfissaoDTO;
import br.unipar.devbackend.fixr.model.Profissao;
import br.unipar.devbackend.fixr.service.ProfissaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profissao")
@CrossOrigin(origins = "*")
public class ProfissaoController {

    private final ProfissaoService service;

    public ProfissaoController(ProfissaoService service) {
        this.service = service;
    }

    @PostMapping
    public Profissao cadastrar(@Valid @RequestBody ProfissaoDTO dto){
        return service.cadastrar(dto);
    }
}