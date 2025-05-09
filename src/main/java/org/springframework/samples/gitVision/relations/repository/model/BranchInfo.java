package org.springframework.samples.gitvision.relations.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BranchInfo {
    private String name;
    private boolean defaultBranch;
}
