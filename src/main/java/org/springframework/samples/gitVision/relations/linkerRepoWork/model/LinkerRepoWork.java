package org.springframework.samples.gitvision.relations.linkerRepoWork.model;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.repository.model.Repository;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.workspace.model.Workspace;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "linker_repo_work", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"repository_id", "workspace_id", "user_id"})
})
public class LinkerRepoWork extends EntityIdSequential {

    @ManyToOne
    Repository repository;

    @ManyToOne
    Workspace workspace;

    @ManyToOne
    User user;

}
