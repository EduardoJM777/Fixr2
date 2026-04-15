package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Profissao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfissaoRepository extends JpaRepository<Profissao, Long> {
}
