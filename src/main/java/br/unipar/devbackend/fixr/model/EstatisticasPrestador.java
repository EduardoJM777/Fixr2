package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Data
public class EstatisticasPrestador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "prestador_id", nullable = false)
    private Prestador prestador;

    // TODO: alimentar via evento ou query
    private Integer avaliacoesRecebidas = 0;

    // TODO: alimentar via evento ou query
    private Integer trabalhosRealizados = 0;

    private Integer rankingPosicao = 0;

    private BigDecimal precoMedio = BigDecimal.ZERO;

    private String experienciaTrabalho;


    @Transient
    public String getTempoNoApp() {
        if (this.prestador == null || this.prestador.getDataCadastro() == null) return "-";
        long meses = ChronoUnit.MONTHS.between(this.prestador.getDataCadastro(), LocalDateTime.now());
        if (meses < 1) return "menos de 1 mês";
        if (meses < 12) return meses + " mês(es)";
        long anos = meses / 12;
        long mesesRestantes = meses % 12;
        return anos + " ano(s)" + (mesesRestantes > 0 ? " e " + mesesRestantes + " mês(es)" : "");

    }

}
