package org.springframework.samples.gitvision.relations.collaborator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.relations.collaborator.model.changesByCollaborator.ChangesByCollaborator;
import org.springframework.stereotype.Service;

@Service
public class CollaboratorService {
    
    CollaboratorRepository collaboratorRepository;

    @Autowired
    public CollaboratorService(CollaboratorRepository collaboratorRepository){
        this.collaboratorRepository = collaboratorRepository;
    }

    public List<GithubUser> getAllCollaboratorsByRepositoryId(Long repositoryId) {
        return this.collaboratorRepository.findAllCollaboratorByRepository_Id(repositoryId);
    }

    public ChangesByCollaborator getNumChangesByCollaborator(Long repositoryId) {
        List<Object[]> res = collaboratorRepository.getNumChangesByCollaborator(repositoryId);
        return ChangesByCollaborator.of(res);
    }
    
}
