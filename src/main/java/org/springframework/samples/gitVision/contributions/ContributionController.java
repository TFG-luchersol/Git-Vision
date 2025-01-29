package org.springframework.samples.gitvision.contributions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.samples.gitvision.auth.payload.response.BadResponse;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
import org.springframework.samples.gitvision.contributions.model.CommitContribution;
import org.springframework.samples.gitvision.util.Information;
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

    public ContributionController(ContributionService contributionService){
        this.contributionService = contributionService;
    }

    @GetMapping("/{owner}/{repo}/between_time")
    public MessageResponse getCommitsByUserBetweenDates(@PathVariable String owner, 
                                                        @PathVariable String repo, 
                                                        @RequestParam String login,
                                                        @RequestParam(required = false) String startDate,
                                                        @RequestParam(required = false) String endDate) {
        try {
            String repositoryName = owner + "/" + repo;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date d1 = startDate == null ? null : dateFormat.parse(startDate);
            Date d2 = endDate == null ? null : dateFormat.parse(endDate);
            List<CommitContribution> contributions = this.contributionService.getContributionsByDateBetweenDates(repositoryName, login, d1, d2); 
            Information information = Information.create("contributions", contributions);
            return OkResponse.of(information);
        } catch (Exception e) {
            return BadResponse.of(e);
        }

    }
}
