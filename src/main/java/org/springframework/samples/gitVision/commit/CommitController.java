package org.springframework.samples.gitVision.commit;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitVision.commit.stats.CommitsByPerson;
import org.springframework.transaction.annotation.Transactional;
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

    @GetMapping("/byUser?startDate={startDate}&endDate={endDate}")
    public List<CommitsByPerson> getNumCommitsByUserInPeriod(
        @RequestParam(required = false) LocalDateTime startDate, 
        @RequestParam(required = false) LocalDateTime endDate)
        {
        return this.commitService.getNumCommitsByUserInPeriod(startDate, endDate);
    }
    

}
