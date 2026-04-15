package br.unipar.devbackend.fixr.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProfissaoDTO(
        @NotNull
        @Size(min = 5)
        String nome,

        @NotNull
        @Size(max = 255)
        String descricao
) {
}
