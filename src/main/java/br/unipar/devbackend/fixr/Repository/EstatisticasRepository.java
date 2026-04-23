package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Estatisticas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstatisticasRepository extends JpaRepository<Estatisticas, Long> {

    Optional<Estatisticas> findByClienteId(Integer clienteId);

}
