package org.springframework.samples.gitvision.commit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.model.repository.RepositoryIdString;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitRepository extends RepositoryIdString<Commit>{

    // Contador de commits por usuario entre unas fechas
    
    @Query("select co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :repositoryId and c.author = co and c.date >= :startDate and c.date <= :endDate) from Collaborator rc join rc.collaborator co on rc.collaborator = co where rc.repository.id = :repositoryId")
    List<Object[]> getNumCommitsByUserOnDate(Long repositoryId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("select co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :repositoryId and c.author = co and c.date >= :startDate) from Collaborator rc join rc.collaborator co on rc.collaborator = co where rc.repository.id = :repositoryId")
    List<Object[]> getNumCommitsByUserAfterThat(Long repositoryId, @Param("startDate") LocalDateTime startDate);

    @Query("select co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :repositoryId and c.author = co and c.date <= :endDate) from Collaborator rc join rc.collaborator co on rc.collaborator = co where rc.repository.id = :repositoryId")
    List<Object[]> getNumCommitsByUserBeforeThat(Long repositoryId, @Param("endDate") LocalDateTime endDate);

    @Query("select co.username, co.avatarUrl, (select count(c) from Commit c where c.repository.id = :repositoryId and c.author = co) from Collaborator rc join rc.collaborator co where rc.repository.id = :repositoryId")
    List<Object[]> getNumCommitsByUser(Long repositoryId);

    // Contador de commits en un repositorio agrupando por un tipo de tiempo

    @Query("select hour(c.date), count(c) from Commit c where c.repository.id = :repositoryId group by hour(c.date)")
    List<Integer[]> getNumCommitsByHour(Long repositoryId);

    @Query("select c.date from Commit c where c.repository.id = :repositoryId")
    List<LocalDateTime> getDateByRepositoryId(Long repositoryId);

    default List<Integer[]> getNumCommitsByDayOfWeek(Long repositoryId) {
        List<LocalDateTime> dates = getDateByRepositoryId(repositoryId);
        return dates.stream().collect(Collectors.groupingBy(date -> date.getDayOfWeek().getValue(), 
                                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)))
                             .entrySet().stream()
                             .map(entry -> new Integer[]{entry.getKey(), entry.getValue()})
                             .toList();
    };

    @Query("select month(c.date), count(c) from Commit c where c.repository.id = :repositoryId group by month(c.date)")
    List<Integer[]> getNumCommitsByMonth(Long repositoryId);

    @Query("select year(c.date), count(c) from Commit c where c.repository.id = :repositoryId group by year(c.date)")
    List<Integer[]> getNumCommitsByYear(Long repositoryId);





}
