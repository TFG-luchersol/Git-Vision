package org.springframework.samples.gitvision.relations.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReleaseInfo {

    private String name;
    private boolean lastRelease;
}
