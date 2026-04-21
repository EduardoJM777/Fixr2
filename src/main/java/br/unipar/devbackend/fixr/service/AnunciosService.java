package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.AnunciosRepository;
import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.Repository.ProfissaoRepository;
import br.unipar.devbackend.fixr.dto.AnuncioRequestDTO;
import br.unipar.devbackend.fixr.dto.AnuncioResponseDTO;
import br.unipar.devbackend.fixr.model.Anuncios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public AnuncioResponseDTO cadastrar(AnuncioRequestDTO dto,
                                        MultipartFile imagem) throws IOException {
        Anuncios anuncios = new Anuncios();
        anuncios.setDescricao(dto.descricao());
        anuncios.setImagem(imagem.getBytes());
        anuncios.setImagemTipo(imagem.getContentType());
        anuncios.setProfissao(proRepository.findById(dto.profissaoId()).orElseThrow());
        anuncios.setCliente(clienteRepository.findById(dto.clienteId()).orElseThrow());

        Anuncio salvo = repository.save(anuncios);

        return toDTO(salvo);
    }

    private AnuncioResponseDTO toDTO(Anuncios anuncios){
        AnuncioResponseDTO response = new AnuncioResponseDTO();
        response.id(anuncios.getId());
        response.descricao(anuncios.getDescricao());
        response.imagemTipo(anuncios.getImagemTipo());
        response.profissaoId(anuncios.getProfissao().getId());
        response.clienteId(anuncios.getCliente().getId());
        response.imagemUrl("/anuncios/" + anuncios.getId() + "/imagem");
        return response;
    }

    public List<Anuncios> listar(){
        return repository.findAll();
    }

    public Anuncios buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
    }

//    public Anuncios atualizar(Long id, AnuncioDTO anuncioDTOAtualizado){
//        return repository.findById(id).map(anuncios -> {
//
//            anuncios.setDescricao(anuncioDTOAtualizado.descricao());
//            Profissao profissao = proRepository.getReferenceById(anuncioDTOAtualizado.idProfissao());
//            anuncios.setProfissao(profissao);
//
//            if (anuncioDTOAtualizado.idCliente() != null) {
//                Cliente cliente = clienteRepository.getReferenceById(anuncioDTOAtualizado.idCliente());
//                anuncios.setCliente(cliente);
//            }
//
//            return repository.save(anuncios);
//        }).orElseThrow(() -> new RuntimeException("Anúncio não encontrado."));
//    }

    public void deletar(Long id){repository.deleteById(id);
    }

}