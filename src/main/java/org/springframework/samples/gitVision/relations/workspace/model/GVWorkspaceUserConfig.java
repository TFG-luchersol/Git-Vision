package org.springframework.samples.gitvision.relations.workspace.model;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.workspace.model.UserProfile;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "gv_workspace_configurations")
@Entity
public class GVWorkspaceUserConfig extends EntityIdSequential {

    @ManyToOne
    private GVWorkspace gvWorkspace;

    @Embedded
    private UserProfile userProfile;

    private String alias;

}
