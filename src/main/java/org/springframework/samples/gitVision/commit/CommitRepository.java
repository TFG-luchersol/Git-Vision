package org.springframework.samples.gitvision.commit;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.commit.model.CommitsByPerson;
import org.springframework.samples.gitvision.model.repository.RepositoryIdString;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitRepository extends RepositoryIdString<Commit> {
    
    @Query("select new org.springframework.samples.gitvision.commit.model.CommitsByPerson(co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :idRepository and c.author = co and c.date >= :startDate and c.date <= :endDate)) from RepositoryCollaborator rc join rc.collaborator co on rc.collaborator = co where rc.repository.id = :idRepository")
    List<CommitsByPerson> getNumCommitsByUserOnDate(Long idRepository, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select new org.springframework.samples.gitvision.commit.model.CommitsByPerson(co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :idRepository and c.author = co and c.date >= :startDate)) from RepositoryCollaborator rc join rc.collaborator co on rc.collaborator = co where rc.repository.id = :idRepository")
    List<CommitsByPerson> getNumCommitsByUserAfterThat(Long idRepository, @Param("startDate") LocalDateTime startDate);

    @Query("select new org.springframework.samples.gitvision.commit.model.CommitsByPerson(co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :idRepository and c.author = co and c.date <= :endDate)) from RepositoryCollaborator rc join rc.collaborator co on rc.collaborator = co where rc.repository.id = :idRepository")
    List<CommitsByPerson> getNumCommitsByUserBeforeThat(Long idRepository, @Param("endDate") LocalDateTime endDate);

    @Query("select new org.springframework.samples.gitvision.commit.model.CommitsByPerson(co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :idRepository and c.author = co)) from RepositoryCollaborator rc join rc.collaborator co where rc.repository.id = :idRepository")
    List<CommitsByPerson> getNumCommitsByUser(Long idRepository);

}
