package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SQLRestriction("ativo = true")
//@table
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String senhaHash;

    private LocalDateTime dataCadastro = LocalDateTime.now();

    private LocalDate dataNascimento;

    private String telefone;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private Boolean ativo = true;

}
