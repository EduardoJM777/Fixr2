package br.unipar.devbackend.fixr.dto;

import br.unipar.devbackend.fixr.model.StatusAcordo;

import java.time.LocalDate;

public record AcordosDTO (

     LocalDate data_servico,

     Double valor,

     Double valor2,

     StatusAcordo statusAcordo,

     Long idChats

){}
