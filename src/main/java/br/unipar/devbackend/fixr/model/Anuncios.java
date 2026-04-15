package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Anuncios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    private String descricao;
    private LocalDateTime dataPublicacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatusAnuncio statusAnuncio = StatusAnuncio.PUBLICADO;

    @ManyToOne
    private Profissao profissao;

    @ManyToOne
    private Cliente cliente;

}
