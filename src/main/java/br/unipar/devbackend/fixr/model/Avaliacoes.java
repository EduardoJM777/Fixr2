package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Avaliacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double nota;
    private String comentario;
    private LocalDate data;
    private String sugest_melhoria;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Prestador prestador;

}
