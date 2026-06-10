package br.unipar.devbackend.fixr.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SQLRestriction("ativo = true")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    private String foto;

    private Boolean online;

}
