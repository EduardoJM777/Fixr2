package br.unipar.devbackend.fixr.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ChatsDTO(

        @NotNull
        @Positive
        Long idPrestador,

        @NotNull
        @Positive
        Long idCliente

) {}