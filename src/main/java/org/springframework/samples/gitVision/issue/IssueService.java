package org.springframework.samples.gitvision.issue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.userRepo.UserRepoRepository;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.samples.gitvision.util.GithubApi;
import org.springframework.stereotype.Service;

@Service
public class IssueService {

    @Autowired
    private UserRepoRepository userRepoRepository;

    public List<Issue> getAllIssuesByRepositoryName(String repositoryName, String login, Integer page)
            throws Exception {
        UserRepo userRepo = this.userRepoRepository.findByNameAndUser_Username(repositoryName, login).get();
        String tokenToUse = userRepo.getDecryptedToken();

        return GithubApi.getIssuesByPage(repositoryName, page, 30, tokenToUse);
    }

    private List<Commit> getCommitsByIssueNumber(GHRepository ghRepository, Integer issueNumber) throws Exception {
        return ghRepository.getIssue(issueNumber).listEvents().toList().stream()
                .map(GHIssueEvent::getCommitId)
                .filter(Objects::nonNull)
                .map(t -> {
                    try {
                        return ghRepository.getCommit(t);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).distinct()
                .map(Commit::parse).toList();
    }

    private Map<String, Change> getChangesByUser(List<Commit> commits) {
        return commits.stream()
                .collect(Collectors.groupingBy(commit -> commit.getAuthor().getUsername(), Collectors.reducing(new Change(),
                        Commit::getChange,
                        (acc, next) -> acc.merge(next))));
    }

    public Map<String, Object> getIssueByRepositoryNameAndIssueNumber(GHRepository ghRepository, Integer issueNumber) throws Exception {
        try {
            Map<String, Object> dict = new HashMap<>();
            GHIssue ghIssue = ghRepository.getIssue(issueNumber);
            Issue issue = Issue.parse(ghIssue);
            List<GithubUser> assigness = ghIssue.getAssignees().stream().map(t -> {
                try {
                    return GithubUser.parse(t);
                } catch (IOException e) {
                    return null;
                }
            }).toList();
            List<Commit> commits = getCommitsByIssueNumber(ghRepository, issueNumber);
            List<String> files = commits.stream().flatMap(i -> i.getFiles().stream()).map(i -> i.getFileName())
                    .distinct().toList();
            Map<String, Change> changesByUser = getChangesByUser(commits);
            dict.put("issue", issue);
            dict.put("commits", commits);
            dict.put("files", files);
            dict.put("changesByUser", changesByUser);
            dict.put("assigness", assigness);
            // if (user.hasClockifyToken()) {
            //     Task task = ClockifyApi.getTaskByWorkspaceIdAndProjectIdAndTaksName("workspaceId", "projectId",
            //             issueNumber.toString(), user.getClockifyToken());
            //     if (task != null) {
            //         Duration duration = task.getDuration();
            //         dict.put("duration", duration);
            //     }
            // }
            return dict;
        } catch (IOException e) {
            return null;
        }
    }

}
