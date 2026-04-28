package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.FavoritosRepository;
import br.unipar.devbackend.fixr.Repository.UsuarioRepository;
import br.unipar.devbackend.fixr.model.Favoritos;
import br.unipar.devbackend.fixr.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritosService {

    @Autowired
    private FavoritosRepository favoritosRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void favoritar(Long usuarioId, Long favoritadoId) {
        if (!favoritosRepository.existsByUsuarioIdAndFavoritadoId(usuarioId, favoritadoId)) {
            Favoritos favorito = new Favoritos();
            favorito.setUsuario(usuarioRepository.findById(usuarioId).orElseThrow());
            favorito.setFavoritado(usuarioRepository.findById(favoritadoId).orElseThrow());
            favoritosRepository.save(favorito);
        }
    }

    public void desfavoritar(Long usuarioId, Long favoritadoId) {
        favoritosRepository.deleteByUsuarioIdAndFavoritadoId(usuarioId, favoritadoId);
    }

    public List<Usuario> listarFavoritos(Long usuarioId) {
        return favoritosRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(Favoritos::getFavoritado)
                .collect(Collectors.toList());
    }
}
