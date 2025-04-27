package org.springframework.samples.gitvision.contributions.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContributionByTime {

    private Integer additions;
    private Integer deletions;
    private Long time;

    public static ContributionByTime createNew() {
        var contributionByTime = new ContributionByTime();
        contributionByTime.setAdditions(0);
        contributionByTime.setDeletions(0);
        contributionByTime.setTime(0L);
        return contributionByTime;
    }

    public void mergeChanges(int additions, int deletions) {
        this.setAdditions(this.additions + additions);
        this.setDeletions(this.deletions + deletions);
    }

    public void mergeChanges(Contribution contribution) {
        this.setAdditions(this.additions + contribution.getAdditions());
        this.setDeletions(this.deletions + contribution.getDeletions());
    }

    public void mergeChanges(ContributionByTime other) {
        this.setAdditions(this.additions + other.additions);
        this.setDeletions(this.deletions + other.deletions);
    }

    public void mergeTime(ContributionByTime other) {
        this.setTime(this.time + other.time);
    }

    public void mergeTime(Long time) {
        this.setTime(this.time + time);
    }

}
