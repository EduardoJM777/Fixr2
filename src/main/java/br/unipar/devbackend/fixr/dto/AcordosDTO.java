package br.unipar.devbackend.fixr.dto;

import br.unipar.devbackend.fixr.model.StatusAcordo;

public record AcordosDTO (

     Double valor,

     Double valor2,

     StatusAcordo statusAcordo,

     Long idChats
){}
