package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.EstatisticasPrestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstatisticasPrestadorRepository extends JpaRepository<EstatisticasPrestador, Long> {

    Optional<EstatisticasPrestador> findByPrestadorId(Long prestadorId);

}
