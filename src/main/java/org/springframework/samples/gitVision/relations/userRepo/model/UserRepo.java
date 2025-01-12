package org.springframework.samples.gitvision.relations.userRepo.model;

import org.hibernate.validator.constraints.URL;
import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_repository", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "repositoryId"})
})
public class UserRepo extends EntityIdSequential {
    
    @ManyToOne
    private User user;

    @NotNull
    private Long repositoryId;

    @URL
    String url_imagen;

    @NotBlank
    @Column(unique = true)
    @Pattern(regexp = "^[^\\s/]+/[^\\s/]+$")
    private String name;

    private String token;

    public boolean hasToken(){
        return token != null;
    }

}
