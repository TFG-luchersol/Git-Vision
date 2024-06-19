package org.springframework.samples.gitVision.commit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

@Service
@RequestMapping("/api/v1/commit")
@Tag(name = "Commit")
public class CommitService {
    
    CommitRepository commitRepository;

    @Autowired
    public CommitService(CommitRepository commitRepository) { 
        this.commitRepository = commitRepository;
    }

}
