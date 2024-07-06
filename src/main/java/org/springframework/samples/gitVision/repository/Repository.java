package org.springframework.samples.gitvision.repository;

import org.springframework.samples.gitvision.model.entity.EntityIdLong;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "repositories")
public class Repository extends EntityIdLong {

    @NotBlank
    private String name;

    private String token;


    public boolean haveToken(){
        return token != null;
    }

}
