package br.unipar.devbackend.fixr.dto;

import br.unipar.devbackend.fixr.model.Mensagens;
import lombok.Data;

@Data
public class ChatsDTO{

    private Long clienteId;
    private Long prestadorId;

    private Long chamadorId;
    private String chamadorNome;
    private Mensagens.PapelRemetente papelChamador;

    private Long destinatarioId;
    private String destinatarioNome;

    private Long anuncioId;
    private String anuncioTitulo;

}