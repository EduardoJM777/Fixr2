package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.EstatisticasPrestadorRepository;
import br.unipar.devbackend.fixr.Repository.PrestadorRepository;
import br.unipar.devbackend.fixr.Repository.ProfissaoRepository;
import br.unipar.devbackend.fixr.dto.EstatisticasPrestadorDTO;
import br.unipar.devbackend.fixr.dto.PrestadorDTO;
import br.unipar.devbackend.fixr.model.EstatisticasPrestador;
import br.unipar.devbackend.fixr.model.Prestador;
import br.unipar.devbackend.fixr.model.Profissao;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PrestadorService {

    private final PrestadorRepository repository;
    private final ProfissaoRepository profissaoRepository;
    private final PasswordEncoder passwordEncoder;
    private final EstatisticasPrestadorRepository estatisticasPrestadorRepository;

    public PrestadorService(PrestadorRepository repository,
                            ProfissaoRepository profissaoRepository,
                            PasswordEncoder passwordEncoder,
                            EstatisticasPrestadorRepository estatisticasPrestadorRepository) {
        this.repository = repository;
        this.profissaoRepository = profissaoRepository;
        this.passwordEncoder = passwordEncoder;
        this.estatisticasPrestadorRepository = estatisticasPrestadorRepository;
    }


    public Prestador cadastrar(PrestadorDTO dto){

        Prestador prestador = new Prestador();
        prestador.setNome(dto.nome());
        prestador.setEmail(dto.email());
        prestador.setDataNascimento(dto.dataNascimento());
        prestador.setSenhaHash(passwordEncoder.encode(dto.senha()));
        prestador.setTelefone(dto.telefone());

        Profissao profissao = profissaoRepository.findById(dto.profissaoId())
                .orElseThrow(() -> new RuntimeException("Profissão não encontrada"));

        prestador.setProfissao(profissao);

        Prestador prestadorSalvo = repository.save(prestador);


        EstatisticasPrestador stats = new EstatisticasPrestador();
        stats.setPrestador(prestadorSalvo);
        estatisticasPrestadorRepository.save(stats);

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
            prestador.setSenhaHash(passwordEncoder.encode(dto.senha()));
            prestador.setTelefone(dto.telefone());

            Profissao profissao = profissaoRepository.findById(dto.profissaoId())
                    .orElseThrow(() -> new RuntimeException("Profissão não encontrada"));

            prestador.setProfissao(profissao);

            return repository.save(prestador);

        }).orElseThrow(() -> new RuntimeException("Erro ao atualizar."));
    }


    public void deletar(Long id){

        Prestador prestador = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prestador não encontrado"));

        prestador.setAtivo(false);

        repository.save(prestador);
    }


    public EstatisticasPrestadorDTO buscarEstatisticas(Integer prestadorId) {

        EstatisticasPrestador stats = estatisticasPrestadorRepository.findByPrestadorId(prestadorId)
                .orElseThrow(() -> new EntityNotFoundException("Estatísticas não encontradas para o prestador " + prestadorId));

        return new EstatisticasPrestadorDTO(
                stats.getAvaliacoesRecebidas(),
                stats.getTrabalhosRealizados(),
                stats.getTempoNoApp(),
                stats.getRankingPosicao(),
                stats.getPrecoMedio(),
                stats.getExperienciaTrabalho()
        );
    }

}