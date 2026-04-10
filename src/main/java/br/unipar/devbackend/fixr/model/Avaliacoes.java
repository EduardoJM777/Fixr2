package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Avaliacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double nota;
    private String comentario;
    private LocalDateTime data = LocalDateTime.now();
    private String sugest_melhoria;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Prestador prestador;

}
