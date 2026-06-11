package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Anuncios;
import br.unipar.devbackend.fixr.model.StatusAnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnunciosRepository extends JpaRepository<Anuncios, Long> {
    List<Anuncios> findByStatusAnuncio(StatusAnuncio status);

    List<Anuncios> findByClienteId(Long clienteId);

    long countByClienteId(Long clienteId);

}
