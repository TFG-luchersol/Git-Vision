package org.springframework.samples.gitvision.relations.repository.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.URL;
import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.samples.gitvision.user.model.GVUser;
import org.springframework.samples.gitvision.util.AESConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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
@Table(name = "gv_repositories", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "repositoryId"})
})
public class GVRepo extends EntityIdSequential {

    @OneToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GVUser user;

    @OneToOne(optional = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private GVWorkspace workspace;

    @NotNull
    private Long repositoryId;

    @URL
    private String url_imagen;

    @NotBlank
    @Column(unique = true)
    @Pattern(regexp = "^[^\\s/]+/[^\\s/]+$")
    private String name;

    @Convert(converter = AESConverter.class)
    private String token;

    public boolean hasToken(){
        return this.token != null;
    }

    public boolean hasLinkedWorkspace(){
        return this.workspace != null;
    }

}
