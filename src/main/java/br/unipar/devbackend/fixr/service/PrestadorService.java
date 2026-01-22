package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.PrestadorRepository;
import br.unipar.devbackend.fixr.dto.PrestadorDTO;
import br.unipar.devbackend.fixr.model.Prestador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestadorService {

    private final PrestadorRepository repository;

    @Autowired
    private PrestadorService(PrestadorRepository prestadorRepository){
        this.repository = prestadorRepository;
    }

    public Prestador salvar(PrestadorDTO prestadorDTO){
        Prestador prestador = new Prestador();
        prestador.setNome(prestadorDTO.nome());
        prestador.setEmail(prestadorDTO.email());
        prestador.setProfissao(prestadorDTO.profissao());
        return repository.save(prestador);
    }

    public List<Prestador> listar(){
        return repository.findAll();
    }

    public Prestador buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Prestador não encontrado."));
    }

    public Prestador atualizar(Long id, PrestadorDTO prestadorDTOatualizado){
        return repository.findById(id).map(prestador -> {
            prestador.setNome(prestadorDTOatualizado.nome());
            prestador.setEmail(prestadorDTOatualizado.email());
            prestador.setProfissao(prestadorDTOatualizado.profissao());
            return repository.save(prestador);
        }).orElseThrow(() -> new RuntimeException("Erro."));
    }

    public void apagar(Long id){
        repository.deleteById(id);
    }

}
