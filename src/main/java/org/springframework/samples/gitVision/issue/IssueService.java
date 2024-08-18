package org.springframework.samples.gitvision.issue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.repository.RepoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IssueService {
    
    IssueRepository issueRepository;
    RepoRepository repoRepository;

    @Autowired
    public IssueService(IssueRepository issueRepository, RepoRepository repoRepository){
        this.issueRepository = issueRepository;
        this.repoRepository = repoRepository;
    }

    @Transactional(readOnly = true)
    public List<Commit> getAllCommitsByIssueNumberAndRepositoryId(Integer issueNumber, Long repositoryId){
        if(!repoRepository.existsById(repositoryId))
            throw new ResourceNotFoundException("Repository", "ID", repositoryId);
        return issueRepository.getAllCommitsByIssueNumberAndRepositoryId(issueNumber, repositoryId);
    }

}
