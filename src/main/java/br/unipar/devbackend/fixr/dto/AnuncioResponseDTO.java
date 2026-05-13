package br.unipar.devbackend.fixr.dto;

public record AnuncioResponseDTO (

        Long id,
        String descricao,
        String imagemTipo,
        Long profissaoId,
        String profissaoNome,
        Long clienteId,
        String clienteNome,
        String imagemUrl

) { }
