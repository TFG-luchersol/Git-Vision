package org.springframework.samples.gitvision.relations.userRepo.model;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.repository.model.Repository;
import org.springframework.samples.gitvision.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_repository")
public class UserRepo extends EntityIdSequential {
    
    @ManyToOne
    User user;

    @ManyToOne
    Repository repository;

}
