package br.unipar.devbackend.fixr.dto;

import java.math.BigDecimal;

public record EstatisticasDTO(

        Integer avaliacoesRecebidas,
        Integer anunciosPublicados,
        String tempoNoApp,
        Integer rankingPosicao,
        BigDecimal precoMedio

) { }
