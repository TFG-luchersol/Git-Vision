package org.springframework.samples.gitvision.relations.linkerRepoWork.model;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "linker_repo_work", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"repositoryId", "workspaceId", "user_id"})
})
public class LinkerRepoWork extends EntityIdSequential {

    @NotNull
    @Column(name = "repositoryId")
    Long repositoryId;

    @NotBlank
    @Column(name = "workspaceId")
    String workspaceId;

    @ManyToOne
    User user;

}
