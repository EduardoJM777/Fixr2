package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.Repository.PrestadorRepository;
import br.unipar.devbackend.fixr.dto.LoginRequestDTO;
import br.unipar.devbackend.fixr.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final ClienteRepository clienteRepository;
    private final PrestadorRepository prestadorRepository;

    public AuthController(AuthService authService,
                          ClienteRepository clienteRepository,
                          PrestadorRepository prestadorRepository){
        this.authService = authService;
        this.clienteRepository = clienteRepository;
        this.prestadorRepository = prestadorRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/logout/{usuarioId}")
    public ResponseEntity<?> logout(@PathVariable Long usuarioId,
                                    @RequestParam String tipo) {
        if ("CLIENTE".equalsIgnoreCase(tipo)) {
            clienteRepository.findById(usuarioId).ifPresent(c -> {
                c.setOnline(false);
                clienteRepository.save(c);
            });
        } else {
            prestadorRepository.findById(usuarioId).ifPresent(p -> {
                p.setOnline(false);
                prestadorRepository.save(p);
            });
        }
        return ResponseEntity.ok().build();
    }

}
