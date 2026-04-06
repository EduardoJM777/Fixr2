package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Prestador;
import br.unipar.devbackend.fixr.model.Profissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import javax.swing.text.html.Option;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
    Optional<Profissao> findByNome(String nome);
}
