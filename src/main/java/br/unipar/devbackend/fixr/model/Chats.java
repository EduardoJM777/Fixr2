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
    private Long id;

    private LocalDateTime dataInicio = LocalDateTime.now();
    private LocalDateTime dataEncerramento;

    @Enumerated(EnumType.STRING)
    private StatusChat status = StatusChat.PENDENTE;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Prestador prestador;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Cliente cliente;

    private Boolean ativo = true;

    public enum StatusChat {
        PENDENTE,
        ATIVO,
        ENCERRADO
    }

    @ManyToOne
    @JoinColumn(name = "anuncio_id")
    private Anuncios anuncio;

}
