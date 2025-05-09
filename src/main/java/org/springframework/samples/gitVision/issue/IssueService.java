package org.springframework.samples.gitvision.issue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueEvent;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.repository.GVRepoRepository;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.util.GithubApi;
import org.springframework.stereotype.Service;

@Service
public class IssueService {

    private final GVRepoRepository gvRepoRepository;

    public IssueService(GVRepoRepository gvRepoRepository) {
        this.gvRepoRepository = gvRepoRepository;
    }

    public List<Issue> getAllIssuesByRepositoryName(String repositoryName, String login, Integer page)
            throws Exception {
        GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Username(repositoryName, login).get();
        return GithubApi.connect(gvRepo)
                .getIssuesByPage(page, 30);
    }

    public List<Issue> getAllIssuesByRepositoryNameWithFilter(String repositoryName, String login, String filterText,
            boolean isRegex, boolean isIssueNumber) throws Exception {
        GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Username(repositoryName, login)
                .orElseThrow(() -> ResourceNotFoundException.of(GVRepo.class));
        String tokenToUse = gvRepo.getToken();
        GitHub gitHub = GitHub.connect(login, tokenToUse);
        Stream<GHIssue> ghIssues;
        if (isRegex) {
            ghIssues = gitHub.getRepository(repositoryName).getIssues(GHIssueState.ALL).stream()
                    .filter(issue -> {
                        try {
                            String title = issue.getTitle();
                            return title != null && title.matches(filterText);
                        } catch (PatternSyntaxException e) {
                            return false;
                        }
                    });
        } else if (isIssueNumber) {
            List<GHIssue> ghIssuesList = new ArrayList<>();
            for (String numStr : filterText.split(",")) {
                int issueNumber = Integer.parseInt(numStr.trim());
                GHIssue issue = gitHub.getRepository(repositoryName).getIssue(issueNumber);
                ghIssuesList.add(issue);
            }
            ghIssues = ghIssuesList.stream();
        } else {
            String filter = "repo:" + repositoryName + " " + filterText + " in:title";
            ghIssues = gitHub.searchIssues().q(filter).list().toList().stream();
        }
        return ghIssues.map(Issue::parse).toList();

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
                .collect(Collectors.groupingBy(commit -> commit.getAuthor().getUsername(),
                        Collectors.reducing(new Change(),
                                Commit::getChange,
                                (acc, next) -> acc.merge(next))));
    }

    public Map<String, Object> getIssueByRepositoryNameAndIssueNumber(GHRepository ghRepository, Integer issueNumber)
            throws Exception {
        try {
            Map<String, Object> dict = new HashMap<>();
            GHIssue ghIssue = ghRepository.getIssue(issueNumber);
            Issue issue = Issue.parse(ghIssue);
            List<GithubUser> assignees = ghIssue.getAssignees().stream().map(t -> {
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
            dict.put("assignees", assignees);
            // if (user.hasClockifyToken()) {
            // Task task =
            // ClockifyApi.getTaskByWorkspaceIdAndProjectIdAndTaksName("workspaceId",
            // "projectId",
            // issueNumber.toString(), user.getClockifyToken());
            // if (task != null) {
            // Duration duration = task.getDuration();
            // dict.put("duration", duration);
            // }
            // }
            return dict;
        } catch (IOException e) {
            return null;
        }
    }

}
