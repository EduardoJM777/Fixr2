package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Anuncios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnunciosRepository extends JpaRepository<Anuncios, Long> {
}
