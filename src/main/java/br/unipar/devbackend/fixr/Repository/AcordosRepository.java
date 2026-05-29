package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Acordos;
import br.unipar.devbackend.fixr.model.StatusAcordo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcordosRepository extends JpaRepository<Acordos, Long> {
        Optional<Acordos> findByChatsIdAndStatusAcordo(Long chatId, StatusAcordo status);
        Optional<Acordos> findTopByChatsIdOrderByIdDesc(Long chatId);
    }
