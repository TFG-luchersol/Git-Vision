package org.springframework.samples.gitvision.contributions.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasicStatistics {
    int commitCount;
    int openIssues;
    int closedIssues;
    int openPRs;
    int closedPRs;
}
