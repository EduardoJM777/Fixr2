package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
public class Prestador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;

    @ManyToOne
    private Profissao profissao;

}
