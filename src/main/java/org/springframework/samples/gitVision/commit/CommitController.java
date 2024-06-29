package org.springframework.samples.gitvision.commit;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.commit.model.CommitsByPerson;
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
    public List<CommitsByPerson> getNumCommitsByUserInPeriod(@RequestParam Long repositoryId,
                                                             @RequestParam(required = false) LocalDateTime startDate, 
                                                             @RequestParam(required = false) LocalDateTime endDate){
        return this.commitService.getNumCommitsByUserInPeriod(repositoryId, startDate, endDate);
    }    

}
