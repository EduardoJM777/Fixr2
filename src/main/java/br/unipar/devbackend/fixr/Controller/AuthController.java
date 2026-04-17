package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.LoginRequestDTO;
import br.unipar.devbackend.fixr.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto){
        return ResponseEntity.ok(authService.login(dto));
    }

}
