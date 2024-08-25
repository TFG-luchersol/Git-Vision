package org.springframework.samples.gitvision.dataExtraction;

import org.springframework.samples.gitvision.repository.RepoRepository;
import org.springframework.samples.gitvision.workspace.WorkspaceRepository;
import org.springframework.stereotype.Service;

@Service
public class GitHubClockifyLinkerService {

    RepoRepository repoRepository;
    WorkspaceRepository workspaceRepository;

    public GitHubClockifyLinkerService(RepoRepository repoRepository, WorkspaceRepository workspaceRepository) {
        this.repoRepository = repoRepository;
        this.workspaceRepository = workspaceRepository;
    }

}
