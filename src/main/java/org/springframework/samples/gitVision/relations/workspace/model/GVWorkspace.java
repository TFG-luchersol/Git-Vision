package org.springframework.samples.gitvision.relations.workspace.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.user.GVUser;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gv_workspaces", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "name"})
})
public class GVWorkspace extends EntityIdSequential {

    @NotBlank
    private String workspaceId;

    @NotBlank
    private String name;
        
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GVUser user;

}
