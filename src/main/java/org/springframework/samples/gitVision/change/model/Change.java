package org.springframework.samples.gitvision.change.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true) 
@JsonInclude(JsonInclude.Include.NON_NULL) 
@Getter
@Setter
public class Change {

    public Change(){
        this.additions = 0;
        this.deletions = 0;
        this.totalChanges = 0;
    }
    
    private Integer additions;

    private Integer deletions;

    private Integer totalChanges;

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

    public static Change staticMerge(Change change1, Change change2) {
        Change change = new Change();
        change.setAdditions(change1.additions + change2.additions);
        change.setDeletions(change1.deletions + change2.deletions);
        change.calcTotalChanges();
        return change;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Change [additions=")
                                  .append(additions)
                                  .append(", deletions=")
                                  .append(deletions)
                                  .append("]").toString();
    }

    
}
