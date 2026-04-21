package br.unipar.devbackend.fixr.dto;

public record AnuncioResponseDTO (

        Integer id,
        String descricao,
        String imagemTipo,
        Long profissaoId,
        Integer clienteId,
        String imagemUrl

) { }
