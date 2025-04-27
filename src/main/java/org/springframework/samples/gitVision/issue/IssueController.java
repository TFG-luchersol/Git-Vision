package org.springframework.samples.gitvision.issue;

import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.repository.GVRepoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/issues")
@Tag(name = "Issues")
@SecurityRequirement(name = "bearerAuth")
public class IssueController {

    private final IssueService issueService;
    private final GVRepoService gvRepoService;

    public IssueController(IssueService issueService, GVRepoService gvRepoService){
        this.issueService = issueService;
        this.gvRepoService = gvRepoService;
    }


    @GetMapping("/{owner}/{repo}")
    public ResponseEntity<List<Issue>> getAllIssuesByRepositoryName(@PathVariable String owner,
                                                        @PathVariable String repo,
                                                        @RequestParam(defaultValue = "1") Integer page,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String login = userDetailsImpl.getUsername();
        String repositoryName = owner + "/" + repo;
        List<Issue> issues = this.issueService.getAllIssuesByRepositoryName(repositoryName, login, page);
        return ResponseEntity.ok(issues);

    }

    @GetMapping("/{owner}/{repo}/{issueNumber}")
    public ResponseEntity<Map<String, Object>> getIssueByRepositoryNameAndIssueNumber(@PathVariable String owner,
                                                    @PathVariable String repo,
                                                    @PathVariable Integer issueNumber,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {

        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        Map<String, Object> dict = this.issueService.getIssueByRepositoryNameAndIssueNumber(ghRepository, issueNumber);
        return ResponseEntity.ok(dict);

    }

}
