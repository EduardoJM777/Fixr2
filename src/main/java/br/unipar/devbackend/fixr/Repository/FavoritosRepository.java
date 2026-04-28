package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Favoritos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritosRepository extends JpaRepository<Favoritos, Long> {
    List<Favoritos> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioIdAndFavoritadoId(Long usuarioId, Long favoritadoId);
    void deleteByUsuarioIdAndFavoritadoId(Long usuarioId, Long favoritadoId);
}
