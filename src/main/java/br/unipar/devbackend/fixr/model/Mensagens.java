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
    private Integer id;

    private String texto;
    private LocalDateTime enviadoEm = LocalDateTime.now();

    @ManyToOne
    private Chats chat;

    private Boolean ativo = true;

}
