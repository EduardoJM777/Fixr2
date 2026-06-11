package br.unipar.devbackend.fixr.dto;


import java.math.BigDecimal;

public record EstatisticasPrestadorDTO(
        Integer avaliacoesRecebidas,
        String tempoNoApp,
        BigDecimal precoMedio,
        String experienciaTrabalho,
        Double ultimaNota
) {}
