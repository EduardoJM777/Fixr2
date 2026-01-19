package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Chats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatsRepository extends JpaRepository<Chats, Long> {
}
