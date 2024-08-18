package org.springframework.samples.gitvision.commit;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.commit.model.commitsByPerson.CommitsByPerson;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.CommitsByTimePeriod;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.TimePeriod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/commits")
@Tag(name = "Commit")
public class CommitController {
    
    CommitService commitService;

    @Autowired
    public CommitController(CommitService commitService){
        this.commitService = commitService;
    }

    @GetMapping("/byCollaborator")
    public CommitsByPerson getNumCommitsByUserInPeriod(@RequestParam Long repositoryId,
                                                             @RequestParam(required = false) LocalDateTime startDate, 
                                                             @RequestParam(required = false) LocalDateTime endDate){
        return this.commitService.getNumCommitsByUserInPeriod(repositoryId, startDate, endDate);
    }    

    @GetMapping("/byTime")
    public CommitsByTimePeriod getNumCommitsGroupByTime(@RequestParam Long repositoryId, 
                                                        @RequestParam TimePeriod timePeriod){
        return this.commitService.getNumCommitsGroupByTime(repositoryId, timePeriod);
    } 

}
