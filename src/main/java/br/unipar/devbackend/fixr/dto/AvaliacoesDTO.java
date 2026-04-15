package br.unipar.devbackend.fixr.dto;

import jakarta.validation.constraints.*;

public record AvaliacoesDTO (

        @DecimalMin(value = "0.0")
        @DecimalMax(value = "5.0")
        Double nota,

        @Size(max = 100)
        String comentario,

        @Size(max = 100)
        String sugest_melhoria,

        @NotNull
        @Positive
        Long idCliente,

        @NotNull
        @Positive
        Long idPrestador

)
{}
