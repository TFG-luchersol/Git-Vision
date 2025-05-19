package org.springframework.samples.gitvision.contributions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.contributions.model.BasicStatistics;
import org.springframework.samples.gitvision.contributions.model.Contribution;
import org.springframework.samples.gitvision.contributions.model.ContributionByTime;
import org.springframework.samples.gitvision.relations.repository.GVRepoService;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.task.TaskService;
import org.springframework.samples.gitvision.util.Checker;
import org.springframework.samples.gitvision.util.MessageResolver;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/contributions")
@Tag(name = "Contributions")
@SecurityRequirement(name = "bearerAuth")
public class ContributionController {

    private final ContributionService contributionService;
    private final GVRepoService gvRepoService;
    private final TaskService taskService;
    private final MessageResolver msg;

    public ContributionController(ContributionService contributionService, GVRepoService gvRepoService,
            TaskService taskService, MessageResolver msg){
        this.contributionService = contributionService;
        this.gvRepoService = gvRepoService;
        this.taskService = taskService;
        this.msg = msg;
    }


    @GetMapping("/{owner}/{repo}/basic_statistics")
    public ResponseEntity<BasicStatistics> getRepositoryBasicStatistics(@PathVariable String owner,
                                           @PathVariable String repo,
                                           @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, userDetailsImpl.getUsername());

        int commitCount = ghRepository.listCommits().toList().size();

        int openIssues = ghRepository.getOpenIssueCount();
        int closedIssues = 0;

        int openPRs = 0;
        int closedPRs = 0;

        for (GHPullRequest pr : ghRepository.queryPullRequests().state(GHIssueState.ALL).list()) {
            if (pr.getState() == GHIssueState.OPEN) {
                openPRs++;
            } else {
                closedPRs++;
            }
        }

        for (GHIssue issue : ghRepository.getIssues(GHIssueState.CLOSED)) {
            if (!issue.isPullRequest()) {
                closedIssues++;
            }
        }

        BasicStatistics basicStatistics = BasicStatistics.builder()
            .commitCount(commitCount)
            .openIssues(openIssues)
            .closedIssues(closedIssues)
            .openPRs(openPRs)
            .closedPRs(closedPRs)
            .build();

        return ResponseEntity.ok(basicStatistics);
    }

    @GetMapping("/{owner}/{repo}/between_time")
    public ResponseEntity<List<Contribution>> getCommitsByUserBetweenDates(@PathVariable String owner,
                                                        @PathVariable String repo,
                                                        @RequestParam(defaultValue = "false") String isFolder,
                                                        @RequestParam(required = false) String path,
                                                        @RequestParam(required = false) String startDate,
                                                        @RequestParam(required = false) String endDate,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String repositoryName = owner + "/" + repo;
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(repositoryName, login);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date d1 = startDate == null ? null : dateFormat.parse(startDate);
        Date d2 = endDate == null ? null : dateFormat.parse(endDate);
        List<Contribution> contributions = isFolder == "" ?
            this.contributionService.getContributionsInFolderByDateBetweenDates(ghRepository, repositoryName, login, path, d1, d2) :
            this.contributionService.getContributionsByDateBetweenDates(repositoryName, login, path, d1, d2);
        return ResponseEntity.ok(contributions);
    }

    @GetMapping("/{owner}/{repo}/issue")
    public ResponseEntity<Map<String, ContributionByTime>> getGvRepoByNameAndUser_Id(@PathVariable String owner,
                                @PathVariable String repo,
                                @RequestParam(required = false) Long issueNumber,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String taskName,
                                @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        Long userId = userDetailsImpl.getId();
        String repositoryName = owner + "/" + repo;
        String message = msg.get("api.v1.contributions.owner.repo.time.get.check_issue_number_and_name");
        Checker.checkOrBadRequest((issueNumber == null) ^ (name == null || name.isBlank()), message);
        GVRepo gvRepo = gvRepoService.getGvRepoByNameAndUser_Id(repositoryName, userId);

        if(taskName == null)
            taskName = name != null ? name : "#" + issueNumber;

        Map<String, ContributionByTime> body = issueNumber != null ?
            this.taskService.getContributionByTime(gvRepo, issueNumber, taskName, userId) :
            this.taskService.getContributionByTime(gvRepo, name, taskName, userId);

        return ResponseEntity.ok(body);
    }

}
