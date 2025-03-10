package org.springframework.samples.gitvision.relations.repository.model;

import org.hibernate.validator.constraints.URL;
import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.samples.gitvision.user.GVUser;
import org.springframework.samples.gitvision.util.AESConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "repository", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "repositoryId"})
})
public class GVRepo extends EntityIdSequential {
    
    @ManyToOne()
    private GVUser user;

    @OneToOne
    private GVWorkspace workspace;

    @NotNull
    private Long repositoryId;

    @URL
    String url_imagen;

    @NotBlank
    @Column(unique = true)
    @Pattern(regexp = "^[^\\s/]+/[^\\s/]+$")
    private String name;

    @Convert(converter = AESConverter.class)
    private String token;

    public boolean hasToken(){
        return token != null;
    }

    public boolean hasLinkedWorkspace(){
        return this.workspace != null;
    }

}
