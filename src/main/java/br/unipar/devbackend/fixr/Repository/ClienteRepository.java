package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query(value = """
    SELECT u.id, u.nome, u.email, u.senha_hash, u.data_cadastro,
           u.data_nascimento, u.telefone, u.user_type, u.ativo,
           u.foto, u.online, u.email_confirmado, u.token_confirmacao
    FROM usuario u
    WHERE u.email = :email
    AND u.user_type = 'CLIENTE'
    AND EXISTS (SELECT 1 FROM cliente c WHERE c.id = u.id)
    """, nativeQuery = true)
    Optional<Cliente> findByEmail(@Param("email") String email);

}
