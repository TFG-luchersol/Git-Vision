package org.springframework.samples.gitvision.commit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.TimePeriod;
import org.springframework.samples.gitvision.contributions.model.CommitContribution;
import org.springframework.samples.gitvision.relations.userRepo.UserRepoService;
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

    @Autowired
    UserRepoService userRepoService;

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
        } catch (Exception e) {
            return null;
        }
    }   
    
    @GetMapping("/{owner}/{repo}/{sha}")
    public MessageResponse getCommitByRepositoryNameAndSha(@PathVariable String owner, 
                                                  @PathVariable String repo, 
                                                  @PathVariable String sha,
                                                  @RequestParam String login ){
        try {
            GHRepository ghRepository = this.userRepoService.getRepository(owner, repo, login);
            Commit commit = this.commitService.getCommitByRepositoryNameAndSha(ghRepository, sha);
            Information information = Information.create("commit", commit);
            return MessageResponse.of(information);
        } catch (Exception e) {
            return null;
        }
    }  

    @GetMapping("/{owner}/{repo}/byTime")
    public Map<TimePeriod, Map<Integer, Long>> getNumCommitsGroupByTime(@PathVariable String owner, @PathVariable String repo, @RequestParam String login){
        try {
            GHRepository ghRepository = this.userRepoService.getRepository(owner, repo, login);
            return this.commitService.getNumCommitsGroupByTime(ghRepository);
        } catch (Exception e) {
            return null;
        }
        
    } 



}
