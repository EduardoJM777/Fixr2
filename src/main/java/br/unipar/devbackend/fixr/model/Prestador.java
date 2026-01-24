package br.unipar.devbackend.fixr.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Prestador extends Usuario{

    @PrePersist
    private void definirTipo(){
        this.setUserType(UserType.PRESTADOR);
    }

    @Enumerated(EnumType.STRING)
    private Profissao profissao;

}
