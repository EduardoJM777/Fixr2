package br.unipar.devbackend.fixr.dto;

import br.unipar.devbackend.fixr.model.StatusAnuncio;

public record AnuncioResponseDTO (

        Long id,
        String descricao,
        String imagemTipo,
        Long profissaoId,
        String profissaoNome,
        Long clienteId,
        String clienteNome,
        String imagemUrl,
        StatusAnuncio statusAnuncio

) { }
