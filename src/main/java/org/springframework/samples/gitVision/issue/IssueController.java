package org.springframework.samples.gitvision.issue;

import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.response.BadResponse;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.userRepo.UserRepoService;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/issues")
public class IssueController {
    
    @Autowired
    IssueService issueService;

    @Autowired
    UserRepoService userRepoService;


    @GetMapping("/{owner}/{repo}")
    public MessageResponse getAllIssuesByRepositoryName(@PathVariable String owner, 
                                                        @PathVariable String repo, 
                                                        @RequestParam String login,
                                                        @RequestParam(defaultValue = "1") Integer page) {
        try {
            String repositoryName = owner + "/" + repo;
            List<Issue> issues = this.issueService.getAllIssuesByRepositoryName(repositoryName, login, page);
            Information information = Information.create("issues", issues);
            return OkResponse.of(information);
        } catch (Exception e) {
            return BadResponse.of(e.getMessage());
        }
        
    }

    @GetMapping("/{owner}/{repo}/{issueNumber}")
    public MessageResponse getIssueByRepositoryNameAndIssueNumber(@PathVariable String owner, 
                                                    @PathVariable String repo, 
                                                    @PathVariable Integer issueNumber, 
                                                    @RequestParam String login) {
        
        try {
            GHRepository ghRepository = this.userRepoService.getRepository(owner, repo, login);
            Map<String, Object> dict = this.issueService.getIssueByRepositoryNameAndIssueNumber(ghRepository, issueNumber);
            Information information = Information.of(dict);
            return OkResponse.of(information);
        } catch (Exception e) {
            return BadResponse.of(e);
        }
    
    }

}
