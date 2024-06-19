package org.springframework.samples.gitVision.commit;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitVision.model.BaseRepository;
import org.springframework.samples.gitVision.user.User;

public interface CommitRepository extends BaseRepository<Commit> {
    
    @Query("select new map(c.author, count(c)) from Commit c" + 
                " where (c.date >= startDate or startDate is null) and"+
                " (c.date <= endDate or endDate is null) group by c.author")
    Map<User, Integer> countCommitsByUserOnDate(LocalDate startDate, LocalDate endDate);
}
