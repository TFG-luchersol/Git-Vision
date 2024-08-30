package org.springframework.samples.gitvision.relations.userRepo.model;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.repository.model.Repository;
import org.springframework.samples.gitvision.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_repository", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "repository_id"})
})
public class UserRepo extends EntityIdSequential {
    
    @ManyToOne
    User user;

    @ManyToOne
    Repository repository;

}
