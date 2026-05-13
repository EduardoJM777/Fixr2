package br.unipar.devbackend.fixr.dto;

import br.unipar.devbackend.fixr.model.Mensagens;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MensagensDTO {

    @NotNull
    @Positive
    private Long chatId;

    private Long remetenteId;
    private String remetenteNome;

    Mensagens.PapelRemetente papelRemetente;

    @NotBlank
    @NotNull
    @Size(max = 300)
    private String texto;

    private Mensagens.TipoMensagem tipo = Mensagens.TipoMensagem.CHAT;

}
