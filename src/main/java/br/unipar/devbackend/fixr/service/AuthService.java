package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.Repository.PrestadorRepository;
import br.unipar.devbackend.fixr.dto.LoginRequestDTO;
import br.unipar.devbackend.fixr.dto.LoginResponseDTO;
import br.unipar.devbackend.fixr.model.Cliente;
import br.unipar.devbackend.fixr.model.Prestador;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final PrestadorRepository prestadorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(ClienteRepository clienteRepository,
                       PrestadorRepository prestadorRepository,
                       PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.prestadorRepository = prestadorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO dto){

        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(dto.email());
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setOnline(true);
            clienteRepository.save(cliente);
            return autenticar(
                    cliente.getAtivo(), cliente.getSenhaHash(),
                    dto.senha(), cliente.getId(),
                    cliente.getNome(), cliente.getEmail(), "CLIENTE"
            );
        }

        Optional<Prestador> prestadorOpt = prestadorRepository.findByEmail(dto.email());
        if (prestadorOpt.isPresent()) {
            Prestador prestador = prestadorOpt.get();
            prestador.setOnline(true);
            prestadorRepository.save(prestador);
            return autenticar(
                    prestador.getAtivo(), prestador.getSenhaHash(),
                    dto.senha(), prestador.getId(),
                    prestador.getNome(), prestador.getEmail(), "PRESTADOR"
            );
        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Email ou senha inválidos"
        );

    }

    private LoginResponseDTO autenticar(Boolean ativo, String senhaBanco,
                                        String senhaDigitada, Long id,
                                        String nome, String email, String tipo){

        if (!ativo) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Usuário inativo"
            );
        }

        if (!passwordEncoder.matches(senhaDigitada, senhaBanco)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Email ou senha inválidos"
            );
        }

        return new LoginResponseDTO(id, nome, email, tipo);

    }

}
