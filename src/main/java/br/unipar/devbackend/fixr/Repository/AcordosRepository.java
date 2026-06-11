package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Acordos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AcordosRepository extends JpaRepository<Acordos, Long> {
        Optional<Acordos> findTopByChatsIdOrderByIdDesc(Long chatId);

    @Query("SELECT a FROM Acordos a JOIN FETCH a.chats c WHERE c.id = :chatId AND a.statusAcordo = 'ATIVO' ORDER BY a.id DESC")
    List<Acordos> findByChatsIdOrdenado(@Param("chatId") Long chatId);

    @Query("SELECT AVG(CASE WHEN a.valor2 IS NOT NULL AND a.valor2 > 0 THEN a.valor2 ELSE a.valor END) FROM Acordos a WHERE a.chats.prestador.id = :prestadorId AND a.statusAcordo = 'FINALIZADO'")
    BigDecimal calcularPrecoMedioPrestador(@Param("prestadorId") Long prestadorId);

    @Query("SELECT AVG(CASE WHEN a.valor2 IS NOT NULL AND a.valor2 > 0 THEN a.valor2 ELSE a.valor END) FROM Acordos a WHERE a.chats.cliente.id = :clienteId AND a.statusAcordo = 'FINALIZADO'")
    BigDecimal calcularPrecoMedioCliente(@Param("clienteId") Long clienteId);

    }
