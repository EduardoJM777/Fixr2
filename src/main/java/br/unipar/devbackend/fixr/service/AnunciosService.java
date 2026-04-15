package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.AnunciosRepository;
import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.dto.AnuncioDTO;
import br.unipar.devbackend.fixr.model.Anuncios;
import br.unipar.devbackend.fixr.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnunciosService {

    private final AnunciosRepository repository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public AnunciosService(AnunciosRepository repository, ClienteRepository clienteRepository){
        this.repository = repository;
        this.clienteRepository = clienteRepository;
    }

    public Anuncios cadastrar(AnuncioDTO anuncioDTO){
        Anuncios anuncios = new Anuncios();
        anuncios.setTitulo(anuncioDTO.titulo());
        anuncios.setDescricao(anuncioDTO.descricao());
        anuncios.setProfissao(anuncioDTO.profissao());

        Cliente cliente = clienteRepository.getReferenceById(anuncioDTO.idCliente());
        anuncios.setCliente(cliente);

        return repository.save(anuncios);
    }

    public List<Anuncios> listar(){
        return repository.findAll();
    }

    public Anuncios buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
    }

    public Anuncios atualizar(Long id, AnuncioDTO anuncioDTOAtualizado){
        return repository.findById(id).map(anuncios -> {

            Cliente cliente = clienteRepository.getReferenceById(anuncioDTOAtualizado.idCliente());
            anuncios.setCliente(cliente);

            anuncios.setTitulo(anuncioDTOAtualizado.titulo());
            anuncios.setDescricao(anuncioDTOAtualizado.descricao());
            anuncios.setProfissao(anuncioDTOAtualizado.profissao());
            return repository.save(anuncios);
        }).orElseThrow(() -> new RuntimeException("Anúncio não encontrado."));
    }

    public void deletar(Long id){repository.deleteById(id);
    }

}
