package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
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


}
