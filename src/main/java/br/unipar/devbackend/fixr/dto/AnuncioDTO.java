package br.unipar.devbackend.fixr.dto;

import br.unipar.devbackend.fixr.model.Profissao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AnuncioDTO(

        @NotBlank
        @NotNull
        @Size(max = 300)
        String descricao,

        @NotNull
        Long idProfissao,


        @Positive
        Long idCliente

) {}
