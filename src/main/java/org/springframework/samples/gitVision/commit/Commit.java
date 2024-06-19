package org.springframework.samples.gitVision.commit;

import java.time.LocalDateTime;

import org.springframework.samples.gitVision.model.BaseEntity;
import org.springframework.samples.gitVision.user.User;

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
public class Commit extends BaseEntity{
    
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
