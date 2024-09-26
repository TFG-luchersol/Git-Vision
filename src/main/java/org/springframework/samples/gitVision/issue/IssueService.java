package org.springframework.samples.gitvision.issue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IssueService {

    @Transactional(readOnly = true)
    public Map<Integer, List<Commit>> getAllCommitsByIssueNumberAndRepositoryId(Integer issueNumber, Long repositoryId) throws IOException{
        GitHub github = GitHub.connect();
        GHRepository ghRepository = github.getRepositoryById(repositoryId);
        Map<Integer, List<Commit>> relation = ghRepository.getIssues(GHIssueState.ALL).stream()
                            .collect(Collectors.toMap(GHIssue::getNumber, v -> new ArrayList<>()));
        List<GHCommit> ghCommits = ghRepository.listCommits().toList();
        for (GHCommit ghCommit : ghCommits) {
            Commit commit = Commit.parse(ghCommit);
            for (Integer number : commit.getIssueNumbers()) {
                if(!relation.containsKey(number)) continue;
                relation.get(number).add(commit);
            }
        }
        return relation;
    }

}
