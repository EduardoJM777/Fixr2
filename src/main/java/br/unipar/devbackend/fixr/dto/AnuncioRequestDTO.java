package br.unipar.devbackend.fixr.dto;

import br.unipar.devbackend.fixr.model.StatusAnuncio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AnuncioRequestDTO(

        @NotBlank
        @NotNull
        @Size(max = 300)
        String descricao,

        @NotNull
        Long profissaoId,

        @Positive
        Long clienteId,

        StatusAnuncio statusAnuncio

) {}
