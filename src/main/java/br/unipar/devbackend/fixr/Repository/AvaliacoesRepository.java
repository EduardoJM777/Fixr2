package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Avaliacoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacoesRepository extends JpaRepository<Avaliacoes, Long> {

    Optional<Avaliacoes> findTopByPrestadorIdOrderByDataDesc(Long prestadorId);

    List<Avaliacoes> findByPrestadorId(Long id);

    long countByPrestadorId(Long prestadorId);

    List<Avaliacoes> findByClienteId(Long id);

    long countByClienteId(Long clienteId);

}
