package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.*;
import br.unipar.devbackend.fixr.dto.EstatisticasPrestadorDTO;
import br.unipar.devbackend.fixr.dto.PrestadorDTO;
import br.unipar.devbackend.fixr.model.*;
import jakarta.persistence.EntityNotFoundException;
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
public class PrestadorService {

    private final PrestadorRepository repository;
    private final ProfissaoRepository profissaoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EstatisticasPrestadorRepository estatisticasPrestadorRepository;
    private final AvaliacoesRepository avaliacoesRepository;
    private final EmailService emailService;
    private final UsuarioRepository  usuarioRepository;
    private final AcordosRepository acordosRepository;
    private final ChatsRepository chatsRepository;

    public PrestadorService(PrestadorRepository repository,
                            ProfissaoRepository profissaoRepository,
                            PasswordEncoder passwordEncoder,
                            EstatisticasPrestadorRepository estatisticasPrestadorRepository,
                            AvaliacoesRepository avaliacoesRepository,
                            EmailService emailService, UsuarioRepository usuarioRepository, AcordosRepository acordosRepository, ChatsRepository chatsRepository) {
        this.repository = repository;
        this.profissaoRepository = profissaoRepository;
        this.passwordEncoder = passwordEncoder;
        this.estatisticasPrestadorRepository = estatisticasPrestadorRepository;
        this.avaliacoesRepository = avaliacoesRepository;
        this.emailService = emailService;
        this.usuarioRepository = usuarioRepository;
        this.acordosRepository = acordosRepository;
        this.chatsRepository = chatsRepository;
    }


    public Prestador cadastrar(PrestadorDTO dto){
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado.");
        }

        Prestador prestador = new Prestador();
        prestador.setNome(dto.nome());
        prestador.setEmail(dto.email());
        prestador.setDataNascimento(dto.dataNascimento());
        prestador.setSenhaHash(passwordEncoder.encode(dto.senha()));
        prestador.setTelefone(dto.telefone());
        prestador.setUserType(UserType.PRESTADOR);
        prestador.setAtivo(false);
        prestador.setEmailConfirmado(false);
        prestador.setTokenConfirmacao(UUID.randomUUID().toString());

        Profissao profissao = profissaoRepository.findById(dto.profissaoId())
                .orElseThrow(() -> new RuntimeException("Profissão não encontrada"));

        prestador.setProfissao(profissao);

        Prestador prestadorSalvo = repository.save(prestador);


        EstatisticasPrestador stats = new EstatisticasPrestador();
        stats.setPrestador(prestadorSalvo);
        estatisticasPrestadorRepository.save(stats);

        emailService.enviarConfirmacao(prestadorSalvo.getEmail(), prestadorSalvo.getTokenConfirmacao());

        return prestadorSalvo;
    }

    public List<Prestador> listar(){
        return repository.findAll();
    }

    public Prestador buscarPorId(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado."));
    }

    public Prestador atualizar(Long id, PrestadorDTO dto){
        return repository.findById(id).map(prestador -> {

            prestador.setNome(dto.nome());
            prestador.setEmail(dto.email());
            prestador.setDataNascimento(dto.dataNascimento());
            prestador.setTelefone(dto.telefone());

            Profissao profissao = profissaoRepository.findById(dto.profissaoId())
                    .orElseThrow(() -> new RuntimeException("Profissão não encontrada"));

            if (dto.senha() != null && !dto.senha().isBlank()) {
                prestador.setSenhaHash(passwordEncoder.encode(dto.senha()));
            }

            prestador.setProfissao(profissao);

            return repository.save(prestador);

        }).orElseThrow(() -> new RuntimeException("Erro ao atualizar."));
    }

    public void atualizarFoto(Long id, MultipartFile foto) throws IOException {
        Prestador prestador = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        String nomeArquivo = UUID.randomUUID() + "_" + foto.getOriginalFilename();
        Path caminho = Paths.get("uploads/" + nomeArquivo);
        Files.createDirectories(caminho.getParent());
        Files.write(caminho, foto.getBytes());

        prestador.setFoto("/uploads/" + nomeArquivo);
        repository.save(prestador);
    }


    public void deletar(Long id) {
        Prestador prestador = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prestador não encontrado"));

        chatsRepository.findByPrestadorIdAndStatus(id, Chats.StatusChat.ATIVO)
                .forEach(chat -> {
                    chat.setStatus(Chats.StatusChat.ENCERRADO);
                    chat.setAtivo(false);
                    chatsRepository.save(chat);
                });

        prestador.setAtivo(false);
        prestador.setEmail("deleted_" + id + "_" + prestador.getEmail());
        repository.save(prestador);
    }


    public EstatisticasPrestadorDTO buscarEstatisticas(Long prestadorId) {
        EstatisticasPrestador stats = estatisticasPrestadorRepository.findByPrestadorId(prestadorId)
                .orElseThrow(() -> new EntityNotFoundException("Estatísticas não encontradas"));

        Double ultimaNota = avaliacoesRepository
                .findTopByPrestadorIdOrderByDataDesc(prestadorId)
                .map(Avaliacoes::getNota)
                .orElse(null);

        long totalAvaliacoes = avaliacoesRepository
                .countByPrestadorIdAndAvaliadorTipo(prestadorId, UserType.CLIENTE);

        BigDecimal precoMedio = acordosRepository
                .calcularPrecoMedioPrestador(prestadorId);

        return new EstatisticasPrestadorDTO(
                (int) totalAvaliacoes,
                stats.getTempoNoApp(),
                precoMedio != null ? precoMedio : BigDecimal.ZERO,
                stats.getExperienciaTrabalho(),
                ultimaNota
        );
    }

    public void atualizarExperiencia(Long prestadorId, String experiencia){
        EstatisticasPrestador stats = estatisticasPrestadorRepository.findByPrestadorId(prestadorId)
                .orElseThrow(() -> new EntityNotFoundException("Estatísticas não encontradas"));
        stats.setExperienciaTrabalho(experiencia);
        estatisticasPrestadorRepository.save(stats);
    }



}