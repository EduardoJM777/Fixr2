package br.unipar.devbackend.fixr.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClienteDTO(

        @NotBlank
        @NotNull
        @Size(max=30)
        String nome,

        @Email
        @NotBlank
        @NotNull
        @Size(max=50)
        String email,

        String senha,

        LocalDate dataNascimento,

        String telefone

) {}
