package org.springframework.samples.gitvision.commit.model.commitsByPerson;

import org.springframework.samples.gitvision.util.ContainerItem;

import lombok.Getter;

@Getter
public class CommitsByPersonItem extends ContainerItem {

    private String username, avatarUrl;
    private Long numCommits;

    public CommitsByPersonItem(Object[] objects) {
        super(objects);
        this.username = String.valueOf(objects[0]);
        this.avatarUrl = String.valueOf(objects[1]);
        this.numCommits = (long) objects[2];
    }
    
}
