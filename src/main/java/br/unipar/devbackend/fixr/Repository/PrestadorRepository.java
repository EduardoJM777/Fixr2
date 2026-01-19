package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
}
