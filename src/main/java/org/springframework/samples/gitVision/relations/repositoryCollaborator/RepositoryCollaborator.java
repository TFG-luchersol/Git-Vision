package org.springframework.samples.gitvision.relations.repositoryCollaborator;

import org.springframework.samples.gitvision.colaborator.Collaborator;
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
@Table(name = "repositories_collaborators")
public class RepositoryCollaborator extends EntityIdLong {
    
    @ManyToOne
    private Collaborator collaborator;

    @ManyToOne
    private Repository repository;

}
