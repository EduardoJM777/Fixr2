package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.model.Usuario;
import br.unipar.devbackend.fixr.service.FavoritosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class FavoritosController {

    @Autowired
    private FavoritosService favoritosService;


    @PostMapping("/{favoritadoId}")
    public ResponseEntity<?> favoritar(@PathVariable Long favoritadoId,
                                       @RequestParam Long usuarioId) {
        favoritosService.favoritar(usuarioId, favoritadoId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Favoritado com sucesso!");
    }

    @DeleteMapping("/{favoritadoId}")
    public ResponseEntity<?> desfavoritar(@PathVariable Long favoritadoId,
                                          @RequestParam Long usuarioId) {
        favoritosService.desfavoritar(usuarioId, favoritadoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarFavoritos(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(favoritosService.listarFavoritos(usuarioId));
    }
}
