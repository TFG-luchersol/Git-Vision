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

    // Contador de commits por usuario entre unas fechas
    
    @Query("select new org.springframework.samples.gitvision.commit.model.CommitsByPerson(co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :repositoryId and c.author = co and c.date >= :startDate and c.date <= :endDate)) from RepositoryCollaborator rc join rc.collaborator co on rc.collaborator = co where rc.repository.id = :repositoryId")
    List<CommitsByPerson> getNumCommitsByUserOnDate(Long repositoryId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select new org.springframework.samples.gitvision.commit.model.CommitsByPerson(co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :repositoryId and c.author = co and c.date >= :startDate)) from RepositoryCollaborator rc join rc.collaborator co on rc.collaborator = co where rc.repository.id = :repositoryId")
    List<CommitsByPerson> getNumCommitsByUserAfterThat(Long repositoryId, @Param("startDate") LocalDateTime startDate);

    @Query("select new org.springframework.samples.gitvision.commit.model.CommitsByPerson(co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :repositoryId and c.author = co and c.date <= :endDate)) from RepositoryCollaborator rc join rc.collaborator co on rc.collaborator = co where rc.repository.id = :repositoryId")
    List<CommitsByPerson> getNumCommitsByUserBeforeThat(Long repositoryId, @Param("endDate") LocalDateTime endDate);

    @Query("select new org.springframework.samples.gitvision.commit.model.CommitsByPerson(co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :repositoryId and c.author = co)) from RepositoryCollaborator rc join rc.collaborator co where rc.repository.id = :repositoryId")
    List<CommitsByPerson> getNumCommitsByUser(Long repositoryId);

    // Contador de commits en un repositorio agrupando por un tipo de tiempo

    @Query("select hour(c.date), count(c) from Commit c where c.repository.id = :repositoryId group by hour(c.date)")
    List<Integer[]> getNumCommitsByHour(Long repositoryId);

    @Query("select function('DAYOFWEEK', c.date), count(c) from Commit c where c.repository.id = :repositoryId group by function('DAYOFWEEK', c.date)")
    List<Integer[]> getNumCommitsByDayOfWeek(Long repositoryId);

    @Query("select month(c.date), count(c) from Commit c where c.repository.id = :repositoryId group by month(c.date)")
    List<Integer[]> getNumCommitsByMonth(Long repositoryId);

    @Query("select year(c.date), count(c) from Commit c where c.repository.id = :repositoryId group by year(c.date)")
    List<Integer[]> getNumCommitsByYear(Long repositoryId);





}
