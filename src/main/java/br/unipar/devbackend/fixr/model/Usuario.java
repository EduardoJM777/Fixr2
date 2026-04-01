package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
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

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Column(nullable = false)
    private LocalDate dtNascimento;


}
