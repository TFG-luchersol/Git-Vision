package org.springframework.samples.gitvision.commit;

import java.time.LocalDateTime;

import org.springframework.samples.gitvision.model.entity.GithubEntity;
import org.springframework.samples.gitvision.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "commits")
public class Commit extends GithubEntity {
    
    String description;

    @NotNull
    LocalDateTime date;

    @ManyToOne
    User author;

    @PositiveOrZero
    int additions;

    @PositiveOrZero
    int deletions;
    
}
