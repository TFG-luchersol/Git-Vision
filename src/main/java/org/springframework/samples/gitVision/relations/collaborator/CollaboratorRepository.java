package org.springframework.samples.gitvision.relations.collaborator;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.collaborator.model.Collaborator;
import org.springframework.stereotype.Repository;

@Repository
public interface CollaboratorRepository extends RepositoryIdLong<Collaborator> {
    
    @Query("select rc.collaborator from Collaborator rc where rc.repository.id = :repositoryId")
    List<GithubUser> findAllCollaboratorByRepository_Id(Long repositoryId);

    @Query("select c.author.username, c.author.avatarUrl, c.additions, c.deletions from Commit c where c.repository.id = :repositoryId")
    List<Object[]> getNumChangesByCollaborator(Long repositoryId);

    Optional<Collaborator> findByCollaborator_IdAndRepository_Id(Long githubId, Long repositoryId);
}
