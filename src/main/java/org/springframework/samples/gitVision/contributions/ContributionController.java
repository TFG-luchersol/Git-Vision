package org.springframework.samples.gitvision.contributions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.kohsuke.github.GHRepository;
import org.springframework.samples.gitvision.auth.payload.response.BadResponse;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.contributions.model.Contribution;
import org.springframework.samples.gitvision.relations.repository.GVRepoService;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/contributions")
@Tag(name = "Commit")
public class ContributionController {
    
    private final ContributionService contributionService;
    private final GVRepoService gvRepoService;

    public ContributionController(ContributionService contributionService, GVRepoService gvRepoService){
        this.contributionService = contributionService;
        this.gvRepoService = gvRepoService;
    }

    @GetMapping("/{owner}/{repo}/between_time")
    public MessageResponse getCommitsByUserBetweenDates(@PathVariable String owner, 
                                                        @PathVariable String repo, 
                                                        @RequestParam(defaultValue = "false") String isFolder,
                                                        @RequestParam(required = false) String path,
                                                        @RequestParam(required = false) String startDate,
                                                        @RequestParam(required = false) String endDate,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        try {
            String repositoryName = owner + "/" + repo;
            String login = userDetailsImpl.getUsername();
            GHRepository ghRepository = this.gvRepoService.getRepository(repositoryName, login);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date d1 = startDate == null ? null : dateFormat.parse(startDate);
            Date d2 = endDate == null ? null : dateFormat.parse(endDate);
            List<Contribution> contributions = isFolder == "" ? 
                this.contributionService.getContributionsInFolderByDateBetweenDates(ghRepository, repositoryName, login, path, d1, d2) :
                this.contributionService.getContributionsByDateBetweenDates(repositoryName, login, path, d1, d2);
            Information information = Information.create("contributions", contributions);
            return OkResponse.of(information);
        } catch (Exception e) {
            return BadResponse.of(e);
        }

    }

}
