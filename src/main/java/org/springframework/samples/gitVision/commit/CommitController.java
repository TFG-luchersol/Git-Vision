package org.springframework.samples.gitvision.commit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.commit.model.commitsByPerson.CommitsByPerson;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.CommitsByTimePeriod;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.TimePeriod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/commits")
@Tag(name = "Commit")
public class CommitController {
    
    @Autowired
    CommitService commitService;

    @GetMapping("/{repositoryId}")
    public List<Commit> getNumCommitsByUserInPeriod(@PathVariable Long repositoryId){
        try {
            return this.commitService.getCommitsByRepositoryId(repositoryId);
        } catch (IOException e) {
            return null;
        }
    }    

    @GetMapping("/byTime")
    public Map<TimePeriod, Map<Integer, Long>> getNumCommitsGroupByTime(@RequestParam Long repositoryId){
        try {
            return this.commitService.getNumCommitsGroupByTime(repositoryId);
        } catch (Exception e) {
            return null;
        }
        
    } 

}
