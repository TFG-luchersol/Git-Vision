package org.springframework.samples.gitvision.relations.linkerRepoWork.model;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "linker_repo_work", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"repository_id", "workspace_id", "user_id"})
})
public class LinkerRepoWork extends EntityIdSequential {

    Long repository_id;

    String workspace_id;

    @ManyToOne
    User user;

}
