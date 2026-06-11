package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.*;
import br.unipar.devbackend.fixr.dto.ClienteDTO;
import br.unipar.devbackend.fixr.dto.EstatisticasDTO;
import br.unipar.devbackend.fixr.model.Cliente;
import br.unipar.devbackend.fixr.model.Estatisticas;
import br.unipar.devbackend.fixr.model.UserType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EstatisticasRepository estatisticasRepository;
    private final AvaliacoesRepository avaliacoesRepository;
    private final AnunciosRepository anunciosRepository;
    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final AcordosRepository acordosRepository;

    @Autowired
    public ClienteService(ClienteRepository repository,
                          PasswordEncoder passwordEncoder,
                          EstatisticasRepository estatisticasRepository,
                          AvaliacoesRepository avaliacoesRepository,
                          AnunciosRepository anunciosRepository,
                          EmailService emailService, UsuarioRepository usuarioRepository, AcordosRepository acordosRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.estatisticasRepository = estatisticasRepository;
        this.avaliacoesRepository = avaliacoesRepository;
        this.anunciosRepository = anunciosRepository;
        this.emailService = emailService;
        this.usuarioRepository = usuarioRepository;
        this.acordosRepository = acordosRepository;
    }

    public Cliente cadastrar(ClienteDTO clienteDTO){
        if (usuarioRepository.findByEmail(clienteDTO.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado.");
        }
        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.nome());
        cliente.setEmail(clienteDTO.email());
        cliente.setDataNascimento(clienteDTO.dataNascimento());
        cliente.setSenhaHash(passwordEncoder.encode(clienteDTO.senha()));
        cliente.setTelefone(clienteDTO.telefone());
        cliente.setUserType(UserType.CLIENTE);
        cliente.setAtivo(false);
        cliente.setEmailConfirmado(false);
        cliente.setTokenConfirmacao(UUID.randomUUID().toString());

        Cliente clienteSalvo = repository.save(cliente);

        Estatisticas stats = new Estatisticas();
        stats.setCliente(clienteSalvo);
        estatisticasRepository.save(stats);

        emailService.enviarConfirmacao(clienteSalvo.getEmail(), clienteSalvo.getTokenConfirmacao());

        return clienteSalvo;
    }

    public List<Cliente> listar(){
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

    public Cliente atualizar(Long id, ClienteDTO clienteDTOAtualizado){

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setNome(clienteDTOAtualizado.nome());
        cliente.setEmail(clienteDTOAtualizado.email());
        cliente.setTelefone(clienteDTOAtualizado.telefone());
        cliente.setDataNascimento(clienteDTOAtualizado.dataNascimento());

        if (clienteDTOAtualizado.senha() != null && !clienteDTOAtualizado.senha().isBlank()) {
            cliente.setSenhaHash(passwordEncoder.encode(clienteDTOAtualizado.senha()));
        }

        return repository.save(cliente);

    }

    public void atualizarFoto(Long id, MultipartFile foto) throws IOException {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        String nomeArquivo = UUID.randomUUID() + "_" + foto.getOriginalFilename();
        Path caminho = Paths.get("uploads/" + nomeArquivo);
        Files.createDirectories(caminho.getParent());
        Files.write(caminho, foto.getBytes());

        cliente.setFoto("/uploads/" + nomeArquivo);
        repository.save(cliente);
    }

    public void deletar(Long id){

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        cliente.setAtivo(false);

        repository.save(cliente);
    }

    public EstatisticasDTO buscarEstatisticas(Long clienteId) {
        Estatisticas stats = estatisticasRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Estatísticas não encontradas"));

        long totalAvaliacoes = avaliacoesRepository
                .countByClienteIdAndAvaliadorTipo(clienteId, UserType.PRESTADOR);

        long totalAnuncios = anunciosRepository.countByClienteId(clienteId);

        BigDecimal precoMedio = acordosRepository
                .calcularPrecoMedioCliente(clienteId);

        return new EstatisticasDTO(
                (int) totalAvaliacoes,
                (int) totalAnuncios,
                stats.getTempoNoApp(),
                precoMedio != null ? precoMedio : BigDecimal.ZERO
        );
    }







}
