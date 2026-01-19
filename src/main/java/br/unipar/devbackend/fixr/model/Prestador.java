package br.unipar.devbackend.fixr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
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
