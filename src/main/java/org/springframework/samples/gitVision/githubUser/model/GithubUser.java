package org.springframework.samples.gitvision.githubUser.model;

import java.util.Objects;

import org.springframework.samples.gitvision.model.entity.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "github_users")
public class GithubUser extends Person {
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GithubUser other = (GithubUser) obj;
        return this.id != null && Objects.equals(other.id, this.id);
    }
    
}
