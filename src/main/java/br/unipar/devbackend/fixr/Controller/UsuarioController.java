package br.unipar.devbackend.fixr.Controller;



import br.unipar.devbackend.fixr.Repository.UsuarioRepository;
import br.unipar.devbackend.fixr.dto.UsuarioDTO;
import br.unipar.devbackend.fixr.service.UsuarioService;
import br.unipar.devbackend.fixr.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    private final UsuarioRepository usuarioRepository;


    public UsuarioController(UsuarioService service, UsuarioRepository usuarioRepository){
        this.usuarioService = service;
        this.usuarioRepository = usuarioRepository;
    }

//    @PostMapping
//    public Usuario cadastrar(@Valid @RequestBody UsuarioDTO usuarioDTO){
//        return usuarioService.cadastrar(usuarioDTO);
//    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO)
    { return usuarioService.atualizar(id, usuarioDTO);}

    @GetMapping
    public List<Usuario> listar(){return usuarioService.listar();}

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id){return usuarioService.buscarPorId(id);}

    @GetMapping("/confirmar-email")
    public ResponseEntity<String> confirmarEmail(@RequestParam String token) {
        return usuarioRepository.findByTokenConfirmacao(token)
                .map(usuario -> {
                    usuario.setAtivo(true);
                    usuario.setEmailConfirmado(true);
                    usuario.setTokenConfirmacao(null);
                    usuarioRepository.save(usuario);
                    return ResponseEntity.ok("Email confirmado com sucesso! Você já pode fazer login.");
                })
                .orElse(ResponseEntity.badRequest().body("Token inválido ou expirado."));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        usuarioService.deletar(id);
    }

}
