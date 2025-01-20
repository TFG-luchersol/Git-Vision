package org.springframework.samples.gitvision.commit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.commit.model.CommitContribution;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.TimePeriod;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/commits")
@Tag(name = "Commit")
public class CommitController {
    
    @Autowired
    CommitService commitService;

    @GetMapping("/{owner}/{repo}")
    public MessageResponse getCommitsByRepository(@PathVariable String owner, 
                                                  @PathVariable String repo, 
                                                  @RequestParam String login,
                                                  @RequestParam(defaultValue = "1") Integer page ){
        try {
            String repositoryName = owner + "/" + repo;
            List<Commit> commits = this.commitService.getCommitsByRepository(repositoryName, login, page);
            Information information = Information.create("commits", commits)
                                                 .put("page", page);
            return MessageResponse.of(information);
        } catch (IOException e) {
            return null;
        }
    }   
    
    @GetMapping("/{owner}/{repo}/{sha}")
    public MessageResponse getCommitByRepositoryNameAndSha(@PathVariable String owner, 
                                                  @PathVariable String repo, 
                                                  @PathVariable String sha,
                                                  @RequestParam String login ){
        try {
            String repositoryName = owner + "/" + repo;
            Commit commit = this.commitService.getCommitByRepositoryNameAndSha(repositoryName, sha, login);
            Information information = Information.create("commit", commit);
            return MessageResponse.of(information);
        } catch (IOException e) {
            return null;
        }
    }  

    @GetMapping("/{owner}/{repo}/byTime")
    public Map<TimePeriod, Map<Integer, Long>> getNumCommitsGroupByTime(@PathVariable String owner, @PathVariable String repo, @RequestParam String login){
        try {
            String repositoryName = owner + "/" + repo;
            return this.commitService.getNumCommitsGroupByTime(repositoryName, login);
        } catch (Exception e) {
            return null;
        }
        
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
            List<CommitContribution> contributions = this.commitService.getContributionsByDateBetweenDates(repositoryName, login, d1, d2); 
            Information information = Information.create("contributions", contributions);
            return MessageResponse.of(information);
        } catch (Exception e) {
            return MessageResponse.of(e.getMessage());
        }

    }

}
