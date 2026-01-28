package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Chats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime dataInicio = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL)
    private Prestador prestador;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cliente cliente;

}
