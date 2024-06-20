package org.springframework.samples.gitVision.commit;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitVision.commit.stats.CommitsByPerson;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommitService {
    
    CommitRepository commitRepository;

    @Autowired
    public CommitService(CommitRepository commitRepository) { 
        this.commitRepository = commitRepository;
    }

    @Transactional(readOnly = true)
    public List<CommitsByPerson> getNumCommitsByUserInPeriod(LocalDateTime startDate, LocalDateTime endDate){
        if(startDate == null && endDate == null)
            return this.commitRepository.getNumCommitsByUser();
        else if(startDate == null)
            return this.commitRepository.getNumCommitsByUserBeforeThat(endDate);
        else if(endDate == null)
            return this.commitRepository.getNumCommitsByUserAfterThat(startDate);
        else
            return this.commitRepository.getNumCommitsByUser();
    }
}
