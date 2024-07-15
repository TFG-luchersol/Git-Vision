package org.springframework.samples.gitvision.relations.issueCommit;

import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.issue.Issue;
import org.springframework.samples.gitvision.model.entity.EntityIdLong;
import org.springframework.samples.gitvision.model.entity.EntityIdSequential;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "issues_commits")
public class IssueCommit extends EntityIdSequential {

    @ManyToOne
    private Issue issue;

    @ManyToOne
    private Commit commit;

}
