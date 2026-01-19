package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Mensagens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagensRepository extends JpaRepository<Mensagens, Long> {
}
