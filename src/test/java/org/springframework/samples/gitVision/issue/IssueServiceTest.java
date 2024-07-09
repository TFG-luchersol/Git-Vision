package org.springframework.samples.gitvision.issue;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.gitvision.commit.model.Commit;

@SpringBootTest
public class IssueServiceTest {

    @Autowired
    IssueService issueService;

    @ParameterizedTest
    @CsvSource({"1,1,2","2,1,2","3,1,2","4,1,1","1,2,0"})
    void testGetAllCommitsByIssueNumberAndRepositoryId(Integer issueNumber, Long repositoryId, Integer expected) {
        List<Commit> commits = issueService.getAllCommitsByIssueNumberAndRepositoryId(issueNumber, repositoryId);
        Integer size = commits.size();
        assertEquals(expected, size);
    }
}
