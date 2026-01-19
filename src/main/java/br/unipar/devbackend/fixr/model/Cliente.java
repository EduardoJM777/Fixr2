package br.unipar.devbackend.fixr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cliente extends Usuario {

    @PrePersist
    private void definirTipo(){
        this.setUserType(UserType.CLIENTE);
    }

}
