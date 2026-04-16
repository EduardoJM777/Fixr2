package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Data
@Entity
@SQLRestriction("ativo = true")
public class Chats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime dataInicio = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.ALL)
    private Prestador prestador;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cliente cliente;

    private Boolean ativo = true;

}
