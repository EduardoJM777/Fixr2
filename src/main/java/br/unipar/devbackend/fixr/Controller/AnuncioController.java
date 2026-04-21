package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.AnuncioRequestDTO;
import br.unipar.devbackend.fixr.dto.AnuncioResponseDTO;
import br.unipar.devbackend.fixr.model.Anuncios;
import br.unipar.devbackend.fixr.service.AnunciosService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/anuncio")
@CrossOrigin(origins="*")
public class AnuncioController {
    private final AnunciosService anuncioservice;

    public AnuncioController(AnunciosService service){
        this.anuncioservice = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AnuncioResponseDTO> cadastrar(
            @RequestPart("dados") AnuncioRequestDTO dto,
            @RequestPart("imagem") MultipartFile imagem) throws IOException {

        return ResponseEntity.ok(anuncioservice.cadastrar(dto, imagem));
    }

//    @PutMapping("/{id}")
//    public Anuncios atualizar(@PathVariable Long id, @Valid @RequestBody AnuncioDTO anuncioDTO){
//        return anuncioservice.atualizar(id, anuncioDTO);
//    }

    @GetMapping
    public List<Anuncios> listar(){
        return anuncioservice.listar();
    }

    @GetMapping("/{id}")
    public Anuncios buscarPorId(@PathVariable Long id){
        return anuncioservice.buscarPorId(id);
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> getImagem(@PathVariable Long id){
        Anuncios anuncios = anuncioservice.buscarPorId(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(anuncios.getImagemTipo()))
                .body(anuncios.getImagem());
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        anuncioservice.deletar(id);
    }


}
