package org.springframework.samples.gitvision.collaborator;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.collaborator.model.Collaborator;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.stereotype.Repository;

@Repository
public interface CollaboratorRepository extends RepositoryIdLong<Collaborator> {
    
    @Query("select rc.collaborator from RepositoryCollaborator rc where rc.repository.id = :repositoryId")
    List<Collaborator> getAllCollaboratorsByRepositoryId(Long repositoryId);

    @Query("select c.author.username, c.author.avatarUrl, c.additions, c.deletions from Commit c where c.repository.id = :repositoryId")
    List<Object[]> getNumChangesByCollaborator(Long repositoryId);
}
