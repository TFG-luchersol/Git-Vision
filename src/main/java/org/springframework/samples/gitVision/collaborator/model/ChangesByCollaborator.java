package org.springframework.samples.gitvision.collaborator.model;

import lombok.Getter;

@Getter
public class ChangesByCollaborator {

    private String username, avatarUrl;
    private Integer additions, deletions;

    private Integer getTotalChanges() {
        return this.additions + this.deletions;
    }

}