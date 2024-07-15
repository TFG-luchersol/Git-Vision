package org.springframework.samples.gitvision.relations.collaborator.model;

import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.model.entity.EntityIdLong;
import org.springframework.samples.gitvision.repository.Repository;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "collaborators")
public class Collaborator extends EntityIdLong {
    
    @ManyToOne
    private GithubUser collaborator;

    @ManyToOne
    private Repository repository;

}
