package org.springframework.samples.gitvision.repository.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.samples.gitvision.model.entity.EntityIdLong;
import org.springframework.samples.gitvision.workspace.model.Workspace;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "repositories")
public class Repository extends EntityIdLong {

    @NotBlank
    @Column(unique = true)
    private String name;

    private String token;

    private LocalDateTime updateDate;

    @OneToOne
    private Workspace workspace;

    public boolean haveToken(){
        return token != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Repository other = (Repository) obj;
        return this.id != null && Objects.equals(other.id, this.id);
    }

}
