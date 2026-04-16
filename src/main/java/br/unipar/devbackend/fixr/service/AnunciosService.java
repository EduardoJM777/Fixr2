package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.AnunciosRepository;
import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.Repository.ProfissaoRepository;
import br.unipar.devbackend.fixr.dto.AnuncioDTO;
import br.unipar.devbackend.fixr.model.Anuncios;
import br.unipar.devbackend.fixr.model.Cliente;
import br.unipar.devbackend.fixr.model.Profissao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.unipar.devbackend.fixr.Repository.ProfissaoRepository;

import java.util.List;

@Service
public class AnunciosService {

    private final AnunciosRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProfissaoRepository proRepository;

    @Autowired
    public AnunciosService(AnunciosRepository repository, ProfissaoRepository proRepository,
                           ClienteRepository clienteRepository){
        this.repository = repository;
        this.clienteRepository = clienteRepository;
        this.proRepository = proRepository;
    }

    public Anuncios cadastrar(AnuncioDTO anuncioDTO) {
        Anuncios anuncios = new Anuncios();
        anuncios.setDescricao(anuncioDTO.descricao());
        Profissao profissao = proRepository.getReferenceById(anuncioDTO.idProfissao());
        anuncios.setProfissao(profissao);
        if (anuncioDTO.idCliente() != null) ;

        Cliente cliente = clienteRepository.getReferenceById(anuncioDTO.idCliente());
        anuncios.setCliente(cliente);
        {
            return repository.save(anuncios);
        }
    }

    public List<Anuncios> listar(){
        return repository.findAll();
    }

    public Anuncios buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
    }

    public Anuncios atualizar(Long id, AnuncioDTO anuncioDTOAtualizado){
        return repository.findById(id).map(anuncios -> {

            anuncios.setDescricao(anuncioDTOAtualizado.descricao());
            Profissao profissao = proRepository.getReferenceById(anuncioDTOAtualizado.idProfissao());
            anuncios.setProfissao(profissao);

            if (anuncioDTOAtualizado.idCliente() != null) {
                Cliente cliente = clienteRepository.getReferenceById(anuncioDTOAtualizado.idCliente());
                anuncios.setCliente(cliente);
            }

            return repository.save(anuncios);
        }).orElseThrow(() -> new RuntimeException("Anúncio não encontrado."));
    }

    public void deletar(Long id){repository.deleteById(id);
    }

}
