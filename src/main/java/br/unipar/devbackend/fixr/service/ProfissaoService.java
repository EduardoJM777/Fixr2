package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.ProfissaoRepository;
import br.unipar.devbackend.fixr.dto.ProfissaoDTO;
import br.unipar.devbackend.fixr.model.Profissao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissaoService {
    private final ProfissaoRepository repository;

    @Autowired
    public ProfissaoService(ProfissaoRepository repository) {
        this.repository = repository;
    }

    public Profissao cadastrar(ProfissaoDTO profissaoDTO){
    Profissao profissao = new Profissao();
    profissao.setNome(profissaoDTO.nome());
    profissao.setDescricao(profissaoDTO.descricao());
    return repository.save(profissao);
    }

    public List<Profissao> findAll(){
        return  repository.findAll();
    }
}
