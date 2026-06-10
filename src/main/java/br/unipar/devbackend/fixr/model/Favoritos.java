package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Favoritos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Usuario favoritado;

}
