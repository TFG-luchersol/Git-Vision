package org.springframework.samples.gitvision.change.model;

import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Change {

    int additions;

    int deletions;

    int totalChanges;

    public void calcTotalChanges(){
        this.totalChanges = this.additions + this.deletions;
    }

    public boolean withChanges(){
        return this.additions > 0 || this.deletions > 0;
    }

    public static Change of(int additions, int deletions){
        Change change = new Change();
        change.setAdditions(additions);
        change.setDeletions(deletions);
        change.calcTotalChanges();
        return change;
    }

    public Change merge(Change other) {
        this.setAdditions(this.additions + other.additions);
        this.setDeletions(this.deletions + other.deletions);
        this.calcTotalChanges();
        return this;
    }

    public static Change merge(Change change1, Change change2) {
        Change change = new Change();
        change.setAdditions(change1.additions + change2.additions);
        change.setDeletions(change1.deletions + change2.deletions);
        change.calcTotalChanges();
        return change;
    }

    @Override
    public String toString() {
        return "Change [additions=" + additions + ", deletions=" + deletions + "]";
    }

    
}
