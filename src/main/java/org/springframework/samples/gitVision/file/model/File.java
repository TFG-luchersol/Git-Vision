package org.springframework.samples.gitvision.file.model;

import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.repository.Repository;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "files", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "path", "repository_id" })
})
public class File extends EntityIdSequential {

    String path;

    String extension;

    @ManyToOne
    Repository repository;

    public void setPath(String path) {
        this.path = path;
        this.calcExtension();
    }

    private void calcExtension() {
        if (this.path == null) {
            this.extension = null;
        } else {
            int dotIndex = this.path.lastIndexOf(".");
            if (dotIndex == -1 || dotIndex == this.path.length() - 1)
                this.extension = null;
            else
                this.extension = this.path.substring(dotIndex + 1);
        }

    }

    public String getFileName() {
        int slashIndex = this.path.lastIndexOf("/");
        return this.path.substring(slashIndex + 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        File other = (File) obj;
        return this.id != null && Objects.equals(other.id, this.id);
    }
}
