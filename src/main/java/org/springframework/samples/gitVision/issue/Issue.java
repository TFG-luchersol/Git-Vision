package org.springframework.samples.gitvision.issue;

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
@Table(name = "issues")
public class Issue extends EntityIdLong {
    
    String title;

    Integer number;

    @ManyToOne(optional = false)
    Repository repository;

}
