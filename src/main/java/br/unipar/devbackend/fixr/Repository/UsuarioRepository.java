package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query(value = """
    SELECT u.*, 
           CASE WHEN c.id IS NOT NULL THEN 1 
                WHEN p.id IS NOT NULL THEN 2 
           END as clazz_,
           p.profissao_id
    FROM usuario u
    LEFT JOIN cliente c ON u.id = c.id
    LEFT JOIN prestador p ON u.id = p.id
    WHERE u.token_confirmacao = :token
    """, nativeQuery = true)
    Optional<Usuario> findByTokenConfirmacao(@Param("token") String token);

    Optional<Usuario> findByEmail(String email);
}
