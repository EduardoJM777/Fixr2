package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "anuncios")
@SQLRestriction("ativo = true")
public class Anuncios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @Column(name = "imagem", columnDefinition = "OID")
    private byte[] imagem;
    private String imagemTipo;

    private String descricao;
    private LocalDateTime dataPublicacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatusAnuncio statusAnuncio = StatusAnuncio.PUBLICADO;

    @ManyToOne
    private Profissao profissao;

    @ManyToOne
    private Cliente cliente;

    private Boolean ativo = true;

}
