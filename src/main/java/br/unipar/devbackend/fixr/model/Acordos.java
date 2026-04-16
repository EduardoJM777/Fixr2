package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Data
@Entity
@SQLRestriction("ativo = true")
public class Acordos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate data_servico;
    private Double valor;
    private Double valor2;

    @Enumerated(EnumType.STRING)
    private StatusAcordo statusAcordo;

    @OneToOne
    private Chats chats;

    private Boolean ativo = true;


}
