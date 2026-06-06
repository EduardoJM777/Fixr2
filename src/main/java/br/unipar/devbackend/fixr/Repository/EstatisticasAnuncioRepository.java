package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.EstatisticasAnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EstatisticasAnuncioRepository extends JpaRepository<EstatisticasAnuncio, Long> {

    Optional<EstatisticasAnuncio> findByAnuncioId(Long anuncioId);

    // Retorna todos os anúncios ordenados por cliques desc para calcular ranking
    @Query("SELECT e FROM EstatisticasAnuncio e ORDER BY e.cliques DESC")
    List<EstatisticasAnuncio> findAllOrderedByCliques();

}
