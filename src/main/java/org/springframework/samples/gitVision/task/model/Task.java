package org.springframework.samples.gitvision.task.model;

import java.time.Duration;

import org.springframework.boot.convert.DurationFormat;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.samples.gitvision.model.entity.EntityIdString;
import org.springframework.samples.gitvision.project.model.Project;
import org.springframework.samples.gitvision.workspace.model.Workspace;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task extends EntityIdString {
    
    private String name;

    @DurationFormat(value = DurationStyle.ISO8601)
    private Duration duration;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Workspace workspace;

}
