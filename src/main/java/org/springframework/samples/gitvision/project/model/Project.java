package org.springframework.samples.gitvision.project.model;

import java.time.Duration;

import org.springframework.boot.convert.DurationFormat;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.samples.gitvision.workspace.model.Workspace;

import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project {

    private String id;

    private String name;

    @DurationFormat(value = DurationStyle.ISO8601)
    private Duration duration;

    @ManyToOne
    private Workspace workspace;

}
