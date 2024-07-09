package org.springframework.samples.gitvision.collaborator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.collaborator.model.Collaborator;
import org.springframework.samples.gitvision.collaborator.model.changesByCollaborator.ChangesByCollaborator;
import org.springframework.stereotype.Service;

@Service
public class CollaboratorService {
    
    CollaboratorRepository collaboratorRepository;

    @Autowired
    public CollaboratorService(CollaboratorRepository collaboratorRepository){
        this.collaboratorRepository = collaboratorRepository;
    }

    public List<Collaborator> getAllCollaboratorsByRepositoryId(Long repositoryId) {
        return this.collaboratorRepository.getAllCollaboratorsByRepositoryId(repositoryId);
    }

    public ChangesByCollaborator getNumChangesByCollaborator(Long repositoryId) {
        List<Object[]> res = collaboratorRepository.getNumChangesByCollaborator(repositoryId);
        return ChangesByCollaborator.of(res);
    }
    
}
