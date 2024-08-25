package org.springframework.samples.gitvision.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.repository.model.Repository;
import org.springframework.samples.gitvision.workspace.WorkspaceRepository;
import org.springframework.samples.gitvision.workspace.model.Workspace;
import org.springframework.stereotype.Service;

@Service
public class RepoService {
    
    RepoRepository repoRepository;
    WorkspaceRepository workspaceRepository;
    
    @Autowired 
    public RepoService(RepoRepository repoRepository, WorkspaceRepository workspaceRepository) {
        this.repoRepository = repoRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public void linkRepositoryWithWorkspace(Long repositoryId, String workspaceId){
        Optional<Repository> optRepository = repoRepository.findById(repositoryId);
        Optional<Workspace> optWorkspace = workspaceRepository.findById(workspaceId);
        if(optRepository.isEmpty())
            throw new ResourceNotFoundException("Repository", "ID", repositoryId);
        if(optWorkspace.isEmpty())
            throw new ResourceNotFoundException("Workspace", "ID", repositoryId);
        Repository repository = optRepository.get();
        Workspace workspace = optWorkspace.get();
        repository.setWorkspace(workspace);
        repoRepository.save(repository);
    }
    
}
