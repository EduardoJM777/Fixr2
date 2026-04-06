package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.PrestadorRepository;
import br.unipar.devbackend.fixr.Repository.ProfissaoRepository;
import br.unipar.devbackend.fixr.dto.PrestadorDTO;
import br.unipar.devbackend.fixr.model.Prestador;
import br.unipar.devbackend.fixr.model.Profissao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestadorService {

    private final PrestadorRepository repository;
    private final ProfissaoRepository profissaoRepository;
    private final ProfissaoService profissaoService;

    public PrestadorService(PrestadorRepository repository,
                            ProfissaoRepository profissaoRepository,
                            ProfissaoService profissaoService) {
        this.repository = repository;
        this.profissaoRepository = profissaoRepository;
        this.profissaoService = profissaoService;
    }

    public Prestador cadastrar(PrestadorDTO dto){

        Prestador prestador = new Prestador();
        prestador.setNome(dto.nome());
        prestador.setEmail(dto.email());

        // 🔥 REGRA PRINCIPAL
        if(dto.profissao().equalsIgnoreCase("outro")){

            Profissao nova = profissaoService.cadastrar(dto.novaProfissao());
            prestador.setProfissao(nova);

        } else {

            Profissao existente = profissaoRepository
                    .findByNome(dto.profissao())
                    .orElseThrow(() -> new RuntimeException("Profissão não encontrada"));

            prestador.setProfissao(existente);
        }

        return repository.save(prestador);
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

            if(dto.profissao().equalsIgnoreCase("outro")){
                Profissao nova = profissaoService.cadastrar(dto.novaProfissao());
                prestador.setProfissao(nova);
            } else {
                Profissao existente = profissaoRepository
                        .findByNome(dto.profissao())
                        .orElseThrow(() -> new RuntimeException("Profissão não encontrada"));

                prestador.setProfissao(existente);
            }

            return repository.save(prestador);

        }).orElseThrow(() -> new RuntimeException("Erro."));
    }

    public void deletar(Long id){
        repository.deleteById(id);
    }
}