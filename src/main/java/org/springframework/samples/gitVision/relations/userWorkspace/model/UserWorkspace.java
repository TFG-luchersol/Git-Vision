package org.springframework.samples.gitvision.relations.userWorkspace.model;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.workspace.model.Workspace;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_workspace", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "name"})
})
public class UserWorkspace extends EntityIdSequential {
    
    @ManyToOne
    private User user;

    @ManyToOne
    private Workspace workspace;

    String name;
}
