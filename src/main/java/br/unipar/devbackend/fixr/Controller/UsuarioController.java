//package br.unipar.devbackend.fixr.Controller;
//
//import br.unipar.devbackend.fixr.dto.UsuarioDTO;
//import br.unipar.devbackend.fixr.service.UsuarioService;
//import br.unipar.devbackend.fixr.model.Usuario;
//import jakarta.validation.Valid;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/usuario")
//@CrossOrigin(origins = "*")
//public class UsuarioController {
//
//    private final UsuarioService usuarioService;
//
//    public UsuarioController(UsuarioService service){
//        this.usuarioService = service;
//    }
//
//    @PostMapping
//    public Usuario cadastrar(@Valid @RequestBody UsuarioDTO usuarioDTO){
//        return usuarioService.cadastrar(usuarioDTO);
//    }
//
//    @PutMapping("/{id}")
//    public Usuario atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO)
//    { return usuarioService.atualizar(id, usuarioDTO);}
//
//    @GetMapping
//    public List<Usuario> listar(){return usuarioService.listar();}
//
//    @GetMapping("/{id}")
//    public Usuario buscarPorId(@PathVariable Long id){return usuarioService.buscarPorId(id);}
//
//    @DeleteMapping("/{id}")
//    public void deletar(@PathVariable Long id){
//        usuarioService.deletar(id);
//    }
//
//}
