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
               u.data_nascimento, u.telefone, u.user_type, u.ativo
        FROM cliente c
        INNER JOIN usuario u ON c.id = u.id
        WHERE u.email = :email        
        """, nativeQuery = true)
    Optional<Cliente> findByEmail(@Param("email") String email);

}
