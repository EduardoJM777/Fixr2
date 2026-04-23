package br.unipar.devbackend.fixr.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Data
public class Estatisticas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // TODO: definir como será alimentado (evento ou query)
    private Integer avaliacoesRecebidas = 0;

    // TODO: definir como será alimentado (evento ou query)
    private Integer anunciosPublicados = 0;

    private Integer rankingPosicao = 0;

    private BigDecimal precoMedio = BigDecimal.ZERO;


    @Transient
    public String getTempoNoApp() {
        if (this.cliente == null || this.cliente.getDataCadastro() == null) return "-";
        long meses = ChronoUnit.MONTHS.between(this.cliente.getDataCadastro(), LocalDateTime.now());
        if (meses < 1) return "menos de 1 mês";
        if (meses < 12) return meses + " mês(es)";
        long anos = meses / 12;
        long mesesRestantes = meses % 12;
        return anos + " ano(s)" + (mesesRestantes > 0 ? " e " + mesesRestantes + " mês(es)" : "");
    }

}
