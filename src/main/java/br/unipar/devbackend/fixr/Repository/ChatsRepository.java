package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Chats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatsRepository extends JpaRepository<Chats, Long> {

    List<Chats> findByClienteIdAndStatus(Long clienteId, Chats.StatusChat status);

    List<Chats> findByPrestadorIdAndStatus(Long prestadorId, Chats.StatusChat status);

    Optional<Chats> findByClienteIdAndPrestadorId(Long clienteId, Long prestadorId);

}
