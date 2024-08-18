package org.springframework.samples.gitvision.issue;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends RepositoryIdLong<Issue> {
    
    @Query("select ic.commit from IssueCommit ic where ic.issue.number = :issueNumber and ic.issue.repository.id = :repositoryId")
    List<Commit> findAllCommitByIssue_NumberAndRepository_Id(Integer issueNumber, Long repositoryId);

    // @Query("select i from Issue i where i.number = :number and i.repository.id = :repositoryId")
    Optional<Issue> findIssueByNumberAndRepository_Id(Integer number, Long repositoryId);
    
}
