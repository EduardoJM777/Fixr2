package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


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
