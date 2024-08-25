package org.springframework.samples.gitvision.issue;

import java.util.Objects;

import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.model.entity.EntityIdLong;
import org.springframework.samples.gitvision.repository.model.Repository;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Issue other = (Issue) obj;
        return this.id != null && Objects.equals(other.id, this.id);
    }

}
