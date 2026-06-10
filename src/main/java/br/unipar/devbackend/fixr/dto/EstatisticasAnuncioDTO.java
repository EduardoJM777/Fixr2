package br.unipar.devbackend.fixr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstatisticasAnuncioDTO {

    private Long anuncioId;
    private Double ctr;
    private Integer visualizacoesUnicas;
    private Integer visualizacoesTotais;
    private Integer rankingPosicao;

}
