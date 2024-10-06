package org.springframework.samples.gitvision.relations.userWorkspace.model;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.user.User;

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
@Table(name = "user_workspace", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "name"})
})
public class UserWorkspace extends EntityIdSequential {

    @NotBlank
    private String workspaceId;

    @NotBlank
    private String name;
        
    @ManyToOne
    private User user;

}
