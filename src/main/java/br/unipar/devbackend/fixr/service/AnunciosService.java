package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.AnunciosRepository;
import br.unipar.devbackend.fixr.Repository.ClienteRepository;
import br.unipar.devbackend.fixr.Repository.ProfissaoRepository;
import br.unipar.devbackend.fixr.dto.AnuncioRequestDTO;
import br.unipar.devbackend.fixr.dto.AnuncioResponseDTO;
import br.unipar.devbackend.fixr.model.Anuncios;
import br.unipar.devbackend.fixr.model.Profissao;
import br.unipar.devbackend.fixr.model.StatusAnuncio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

        Anuncios salvo = repository.save(anuncios);

        return toDTO(salvo);
    }

    public List<AnuncioResponseDTO> listar(){
        return repository.findAll().stream()
                .map(this::toDTO)
                .filter(dto -> dto != null)
                .toList();
    }

    @Transactional
    public List<AnuncioResponseDTO> listarPublicados() {
        return repository.findByStatusAnuncio(StatusAnuncio.PUBLICADO).stream()
                .map(this::toDTO)
                .filter(dto -> dto != null)
                .toList();
    }

    public Anuncios buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));
    }

    public AnuncioResponseDTO atualizar(Long id, AnuncioRequestDTO dto){
        Anuncios anuncio = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Anúncio não encontrado: " + id));

        Profissao profissao = proRepository.findById(dto.profissaoId())
                .orElseThrow(() -> new EntityNotFoundException("Profissão não encontrada: " + dto.profissaoId()));

        anuncio.setDescricao(dto.descricao());
        anuncio.setProfissao(profissao);

        if (dto.statusAnuncio() != null) {
            anuncio.setStatusAnuncio(dto.statusAnuncio());
        }

        return toDTO(repository.save(anuncio));
    }

    public void deletar(Long id){repository.deleteById(id);
    }



    private AnuncioResponseDTO toDTO(Anuncios anuncios){
        if (anuncios.getCliente() == null) return null;

        return new AnuncioResponseDTO(
                anuncios.getId(),
                anuncios.getDescricao(),
                anuncios.getImagemTipo(),
                anuncios.getProfissao().getId(), anuncios.getProfissao().getNome(),
                anuncios.getCliente().getId(), anuncios.getCliente().getNome(),
                "/anuncio/" + anuncios.getId() + "/imagem",
                anuncios.getStatusAnuncio()
        );
    }

    public AnuncioResponseDTO buscarPorIdDTO(Long id){
        return toDTO(buscarPorId(id));
    }

    @Transactional
    public List<AnuncioResponseDTO> listarPorCliente(Long clienteId) {
        return repository.findByClienteId(clienteId).stream()
                .map(this::toDTO)
                .filter(dto -> dto != null)
                .toList();
    }


}