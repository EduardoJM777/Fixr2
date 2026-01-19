package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.AnuncioDTO;
import br.unipar.devbackend.fixr.model.Anuncios;
import br.unipar.devbackend.fixr.service.AnunciosService;
import br.unipar.devbackend.fixr.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anuncio")
@CrossOrigin(origins="*")
public class AnuncioController {
    private final AnunciosService anuncioservice;

    public AnuncioController(AnunciosService service){
        this.anuncioservice = service;
    }

    @PostMapping
    public Anuncios salvar(@Valid @RequestBody AnuncioDTO anuncioDTO){
        return anuncioservice.salvar(anuncioDTO);
    }

    @PutMapping("/{id}")
    public Anuncios atualizar(@PathVariable Long id, @RequestBody Anuncios anuncios){
        return anuncioservice.atualizar(id, anuncios);
    }

    @GetMapping
    public List<Anuncios> listar(){
        return anuncioservice.listar();
    }

    @GetMapping("/{id}")
    public Anuncios buscarPorId(@PathVariable Long id){
        return anuncioservice.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void apagarAnuncio(@PathVariable Long id){
        anuncioservice.apagarAnuncio(id);
    }


}
