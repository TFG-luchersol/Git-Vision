package org.springframework.samples.gitvision.workspace.model;

import org.springframework.samples.gitvision.model.entity.EntityIdString;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workspaces")
public class Workspace extends EntityIdString {
    
    private String name;
}
