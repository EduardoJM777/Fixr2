package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Data
@Entity
@SQLRestriction("ativo = true")
public class Mensagens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;
    private LocalDateTime enviadoEm = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private TipoMensagem tipo = TipoMensagem.CHAT;

    @ManyToOne
    private Chats chat;

    @ManyToOne
    private Usuario remetente;

    @Enumerated(EnumType.STRING)
    private PapelRemetente papelRemetente;

    private Boolean ativo = true;

    public enum TipoMensagem {
        CHAT, JOIN, LEAVE, CALL_REQUEST, CALL_ACCEPTED, CALL_REJECTED
    }

    public enum PapelRemetente {
        CLIENTE, PRESTADOR
    }

}
