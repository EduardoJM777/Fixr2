package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "estatisticas_anuncio")
public class EstatisticasAnuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "anuncio_id", nullable = false, unique = true)
    private Anuncios anuncio;

    private Integer cliques = 0;
    private Integer visualizacoesUnicas = 0;
    private Integer visualizacoesTotais = 0;
    private Integer rankingPosicao = 0;

    public Double calcularCTR() {
        if (visualizacoesTotais == null || visualizacoesTotais == 0) return 0.0;
        return (cliques.doubleValue() / visualizacoesTotais.doubleValue()) * 100;
    }


}
