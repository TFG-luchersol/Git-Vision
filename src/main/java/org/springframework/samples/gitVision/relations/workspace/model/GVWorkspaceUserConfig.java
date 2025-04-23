package org.springframework.samples.gitvision.relations.workspace.model;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.workspace.model.UserProfile;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gv_workspace_configurations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"gv_workspace_id", "user_profile_id"})
})
public class GVWorkspaceUserConfig extends EntityIdSequential {

    @ManyToOne
    private GVWorkspace gvWorkspace;

    @Embedded
    private UserProfile userProfile;

    private String alias;

    public static GVWorkspaceUserConfig of(GVWorkspace gvWorkspace, UserProfile userProfile) {
        GVWorkspaceUserConfig config = new GVWorkspaceUserConfig();
        config.setGvWorkspace(gvWorkspace);
        config.setUserProfile(userProfile);
        return config;
    }

}
