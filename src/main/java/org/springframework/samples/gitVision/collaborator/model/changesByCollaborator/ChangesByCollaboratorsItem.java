package org.springframework.samples.gitvision.collaborator.model.changesByCollaborator;

import org.springframework.samples.gitvision.util.ContainerItem;

import jakarta.persistence.Transient;

public class ChangesByCollaboratorsItem extends ContainerItem {
    
    private String username, avatarUrl;
    private Integer additions, deletions;

    public ChangesByCollaboratorsItem(Object[] objects) {
        super(objects);
        this.username = String.valueOf(objects[0]);
        this.avatarUrl = String.valueOf(objects[1]);
        this.additions = (int) objects[2];
        this.deletions = (int) objects[3];
    }

    @Transient
    private Integer getTotalChanges() {
        return this.additions + this.deletions;
    }
}
