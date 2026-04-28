package br.unipar.devbackend.fixr.dto;


import java.math.BigDecimal;

public record EstatisticasPrestadorDTO(

        Integer avaliacoesRecebidas,
        Integer trabalhosRealizados,
        String tempoNoApp,
        Integer rankingPosicao,
        BigDecimal precoMedio,
        String experienciaTrabalho

) {}
