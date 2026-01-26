package br.unipar.devbackend.fixr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MensagensDTO(

        @NotBlank
        @NotNull
        @Size(max = 300)
        String texto,


        Long idChat

) {}
