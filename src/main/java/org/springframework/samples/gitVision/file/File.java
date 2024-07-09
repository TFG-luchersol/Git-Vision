package org.springframework.samples.gitvision.file;

import org.springframework.samples.gitvision.model.entity.EntityIdSequential;
import org.springframework.samples.gitvision.repository.Repository;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;

@Entity
@Getter
@Table(name = "files")
public class File extends EntityIdSequential {

    String path;

    String extension;

    @ManyToOne
    Repository repository;
    
    @Transient
    public String calculeExtension(){
        int dotIndex = this.path.lastIndexOf(".");
        return dotIndex == -1 || dotIndex == this.path.length() - 1 ? "Unknown" : path.substring(dotIndex + 1);
    }

    @Transient
    public String getFileName(){
        int slashIndex = this.path.lastIndexOf("/");
        return this.path.substring(slashIndex + 1);
    }
}
