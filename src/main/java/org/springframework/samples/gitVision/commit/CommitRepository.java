package org.springframework.samples.gitVision.commit;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.gitVision.commit.stats.CommitsByPerson;
import org.springframework.samples.gitVision.model.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitRepository extends BaseRepository<Commit> {
    
    @Query("select new org.springframework.samples.gitVision.commit.stats.CommitsByPerson(u.username, u.avatarUrl, (select count(c) from Commit c where c.author = u and c.date >= :startDate and c.date <= :endDate)) from User u")
    List<CommitsByPerson> getNumCommitsByUserOnDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select new org.springframework.samples.gitVision.commit.stats.CommitsByPerson(u.username, u.avatarUrl, (select count(c) from Commit c where c.author = u and c.date >= :startDate)) from User u")
    List<CommitsByPerson> getNumCommitsByUserAfterThat(@Param("startDate") LocalDateTime startDate);

    @Query("select new org.springframework.samples.gitVision.commit.stats.CommitsByPerson(u.username, u.avatarUrl, (select count(c) from Commit c where c.author = u and c.date <= :endDate)) from User u")
    List<CommitsByPerson> getNumCommitsByUserBeforeThat(@Param("endDate") LocalDateTime endDate);

    @Query("select new org.springframework.samples.gitVision.commit.stats.CommitsByPerson(u.username, u.avatarUrl, (select count(c) from Commit c where c.author = u)) from User u")
    List<CommitsByPerson> getNumCommitsByUser();

}
