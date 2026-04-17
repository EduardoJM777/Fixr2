package br.unipar.devbackend.fixr.Repository;

import br.unipar.devbackend.fixr.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query(value = "SELECT * FROM cliente WHERE email = :email", nativeQuery = true)
    Optional<Cliente> findByEmail(@Param("email") String email);

}
