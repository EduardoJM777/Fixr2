package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Prestador extends Usuario {

    @ManyToOne
    private Profissao profissao;

    @PrePersist
    private void definirTipo(){
        this.setUserType(UserType.PRESTADOR);
    }

}
