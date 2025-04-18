package org.springframework.samples.gitvision.task.model;

import java.time.Duration;

import org.springframework.boot.convert.DurationFormat;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.samples.gitvision.project.model.Project;
import org.springframework.samples.gitvision.workspace.model.Workspace;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {

    private String id;

    private String name;

    @DurationFormat(value = DurationStyle.ISO8601)
    private Duration duration;

    private Project project;

    private Workspace workspace;

    private String[] userGroupIds;

}
