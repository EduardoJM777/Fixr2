package br.unipar.devbackend.fixr.dto;

import br.unipar.devbackend.fixr.model.StatusAcordo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record AcordosDTO (

     LocalDate data_servico,

     @NotBlank
     @NotNull
     Double valor,

     @NotBlank
     @NotNull
     Double valor2,

     @NotNull
     StatusAcordo statusAcordo,

     @NotNull
     @Positive
     Long idChats

){}
