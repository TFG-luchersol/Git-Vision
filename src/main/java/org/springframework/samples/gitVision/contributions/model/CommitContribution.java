package org.springframework.samples.gitvision.contributions.model;

import lombok.Data;

@Data
public class CommitContribution {
    private String committedDate;
    private int additions;
    private int deletions;
    private String authorName;
    private String authorEmail;
}
