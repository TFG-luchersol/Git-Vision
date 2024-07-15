package org.springframework.samples.gitvision.file;

import java.util.Objects;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.repository.Repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "files")
public class File extends EntityIdSequential {

    @Column(unique = true)
    String path;

    String extension;

    @ManyToOne
    Repository repository;
    
    public String calculeExtension(){
        int dotIndex = this.path.lastIndexOf(".");
        return dotIndex == -1 || dotIndex == this.path.length() - 1 ? "Unknown" : path.substring(dotIndex + 1);
    }

    public String getFileName(){
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
