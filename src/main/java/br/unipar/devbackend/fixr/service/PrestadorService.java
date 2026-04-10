package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.PrestadorRepository;
import br.unipar.devbackend.fixr.Repository.ProfissaoRepository;
import br.unipar.devbackend.fixr.dto.LoginDTO;
import br.unipar.devbackend.fixr.dto.PrestadorDTO;
import br.unipar.devbackend.fixr.model.Prestador;
import br.unipar.devbackend.fixr.model.Profissao;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PrestadorService {

    private final PrestadorRepository repository;
    private final ProfissaoRepository profissaoRepository;

    public PrestadorService(PrestadorRepository repository,
                            ProfissaoRepository profissaoRepository) {
        this.repository = repository;
        this.profissaoRepository = profissaoRepository;
    }


    public Prestador cadastrar(PrestadorDTO dto){

        Prestador prestador = new Prestador();
        prestador.setNome(dto.nome());
        prestador.setEmail(dto.email());
        prestador.setDataNascimento(dto.dataNascimento());
        prestador.setSenhaHash(dto.senha());
        prestador.setTelefone(dto.telefone());

//        Profissao profissao = profissaoRepository.findById(dto.profissaoId())
//                .orElseThrow(() -> new RuntimeException("Profissão não encontrada"));
//
//        prestador.setProfissao(profissao);

        return repository.save(prestador);
    }

    public boolean login(LoginDTO dto){
        Prestador prestador = repository.findByEmail(dto.getEmail()).orElse(null);

        if (prestador != null && prestador.getSenhaHash().equals(dto.getSenha())) {
            return true;
        }
        return false;
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
            prestador.setSenhaHash(dto.senha());
            prestador.setTelefone(dto.telefone());

//            Profissao profissao = profissaoRepository.findById(dto.profissaoId())
//                    .orElseThrow(() -> new RuntimeException("Profissão não encontrada"));
//
//            prestador.setProfissao(profissao);

            return repository.save(prestador);

        }).orElseThrow(() -> new RuntimeException("Erro ao atualizar."));
    }


    public void deletar(Long id){
        repository.deleteById(id);
    }
}