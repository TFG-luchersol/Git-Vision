package org.springframework.samples.gitvision.change.model;

import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Change {

    File file;
 
    GithubUser author;

    int additions;

    int deletions;

    int totalChanges;

    public void calcTotalChanges(){
        this.totalChanges = this.additions + this.deletions;
    }

    public boolean withChanges(){
        return this.additions > 0 || this.deletions > 0;
    }
    
    public boolean isDeletedFileChange(){
        return this.file == null;
    }
}
