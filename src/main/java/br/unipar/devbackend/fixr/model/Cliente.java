package br.unipar.devbackend.fixr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Data
public class Cliente extends Usuario {

    @PrePersist
    private void definirTipo(){
        this.setUserType(UserType.CLIENTE);
    }


}
