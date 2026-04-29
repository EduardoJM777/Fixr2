package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Mensagens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagensRepository extends JpaRepository<Mensagens, Long> {

    List<Mensagens> findByChatIdOrderByEnviadoEmAsc(Long chatId);

    List<Mensagens> findTop50ByChatIdOrderByEnviadoEmDesc(Long chatId);

}
