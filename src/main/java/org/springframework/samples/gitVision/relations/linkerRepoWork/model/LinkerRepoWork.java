package org.springframework.samples.gitvision.relations.linkerRepoWork.model;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.relations.userWorkspace.model.UserWorkspace;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "linker_repo_work", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_repo_id", "user_workspace_id"})
})
public class LinkerRepoWork extends EntityIdSequential {

    @NotNull
    @ManyToOne
    UserRepo userRepo;
    
    @NotNull
    @ManyToOne
    UserWorkspace userWorkspace;

}
