package org.springframework.samples.gitvision.file.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileChangeDetail {
    private String commitSha;
    private String patch;
    private Integer additions;
    private Integer deletions;
}
