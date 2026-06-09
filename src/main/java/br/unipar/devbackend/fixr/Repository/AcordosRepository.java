package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Acordos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcordosRepository extends JpaRepository<Acordos, Long> {
}
