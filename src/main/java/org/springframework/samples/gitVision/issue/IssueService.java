package org.springframework.samples.gitvision.issue;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.userRepo.UserRepoRepository;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class IssueService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepoRepository userRepoRepository;

    public List<Issue> getAllIssuesByRepositoryName(String repositoryName, String login, Integer page) throws IOException {
        User user = this.userRepository.findByUsername(login).get();
        UserRepo userRepo = this.userRepoRepository.findByNameAndUser_Id(repositoryName, user.getId()).get();
        String tokenToUse = Objects.requireNonNullElse(userRepo.getToken(), user.getGithubToken());
        GitHub gitHub = GitHub.connect(login, tokenToUse);
        List<Issue> issues = gitHub.getRepository(repositoryName).getIssues(GHIssueState.ALL)
                .stream().map(Issue::parse).toList();
        return issues;
    }

    private List<Commit> getCommitsByIssueNumber(String repositoryName, Integer issueNumber, String login) throws IOException {
        User user = this.userRepository.findByUsername(login).get();
        UserRepo userRepo = this.userRepoRepository.findByNameAndUser_Id(repositoryName, user.getId()).get();
        String tokenToUse = Objects.requireNonNullElse(userRepo.getToken(), user.getGithubToken());
        GitHub gitHub = GitHub.connect(login, tokenToUse);
        GHRepository ghRepository = gitHub.getRepository(repositoryName);
        return ghRepository.getIssue(issueNumber).listEvents().toList().stream()
                    .map(GHIssueEvent::getCommitId)
                    .filter(Objects::nonNull)
                    .map(t -> {
                        try {
                            return ghRepository.getCommit(t);
                        } catch (IOException e) {
                            throw new RuntimeException(e); 
                        }
                    })
                    .map(Commit::parse).toList();
    }

    public Issue getIssueByRepositoryNameAndIssueNumber(String repositoryName, Integer issueNumber, String login) {
        try {
            User user = this.userRepository.findByUsername(login).get();
            UserRepo userRepo = this.userRepoRepository.findByNameAndUser_Id(repositoryName, user.getId()).get();
            String tokenToUse = Objects.requireNonNullElse(userRepo.getToken(), user.getGithubToken());
            GitHub gitHub = GitHub.connect(login, tokenToUse);
            GHRepository ghRepository = gitHub.getRepository(repositoryName);
            return Issue.parse(ghRepository.getIssue(issueNumber));
        } catch (IOException e) {
            return null;
        }
    }


}
