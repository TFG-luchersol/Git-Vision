package org.springframework.samples.gitvision.issue;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends RepositoryIdLong<Issue> {
    
    @Query("select ic.commit from IssueCommit ic where ic.issue.number = :issueNumber and ic.issue.repository.id = :repositoryId")
    List<Commit> getAllCommitsByIssueNumberAndRepositoryId(Integer issueNumber, Long repositoryId);

}
