package br.unipar.devbackend.fixr.dto;

public record AnuncioResponseDTO (

        Long id,
        String descricao,
        String imagemTipo,
        Long profissaoId,
        Long clienteId,
        String imagemUrl

) { }
