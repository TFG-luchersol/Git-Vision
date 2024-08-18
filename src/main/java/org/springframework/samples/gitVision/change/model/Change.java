package org.springframework.samples.gitvision.change.model;

import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.repository.Repository;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "changes")
public class Change extends EntityIdSequential {

    @ManyToOne
    File file;
 
    @OneToOne
    GithubUser author;

    @PositiveOrZero
    int additions;

    @PositiveOrZero
    int deletions;

    @PositiveOrZero
    int totalChanges;

    @PrePersist
    @PreUpdate
    public void calcTotalChanges(){
        this.totalChanges = this.additions + this.deletions;
    }

    public boolean withChanges(){
        return this.additions > 0 || this.deletions > 0;
    }
    
}
