package org.springframework.samples.gitvision.commit;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.commit.model.commitsByPerson.CommitsByPerson;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.CommitsByTimePeriod;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.TimePeriod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommitService {

    CommitRepository commitRepository;

    @Autowired
    public CommitService(CommitRepository commitRepository) {
        this.commitRepository = commitRepository;
    }

    @Transactional(readOnly = true)
    public CommitsByPerson getNumCommitsByUserInPeriod(Long repositoryId, LocalDateTime startDate,
            LocalDateTime endDate) {
        List<Object[]> res = null;
        if (startDate == null && endDate == null)
            res = this.commitRepository.getNumCommitsByUserOnDate(repositoryId, startDate, endDate);
        else if (startDate == null)
            res = this.commitRepository.getNumCommitsByUserBeforeThat(repositoryId, endDate);
        else if (endDate == null)
            res = this.commitRepository.getNumCommitsByUserAfterThat(repositoryId, startDate);
        else
            res = this.commitRepository.getNumCommitsByUser(repositoryId);

        return CommitsByPerson.of(res);
    }

    @Transactional(readOnly = true)
    public CommitsByTimePeriod getNumCommitsGroupByTime(Long repositoryId, TimePeriod timePeriod) {
        List<Integer[]> res = switch (timePeriod) {
            case HOUR -> commitRepository.getNumCommitsByHour(repositoryId);
            case DAY_OF_WEEK -> commitRepository.getNumCommitsByDayOfWeek(repositoryId);
            case MONTH -> commitRepository.getNumCommitsByMonth(repositoryId);
            case YEAR -> commitRepository.getNumCommitsByYear(repositoryId);
        };
        return CommitsByTimePeriod.of(res, timePeriod);
    }

    // @Transactional
    // public void saveCommit(GHCommit ghCommit) {
    //     Commit commit = new Commit();
    //     try {
    //         commit.setMessage(ghCommit.getCommitShortInfo().getMessage());
    //         // commit.setAuthor(ghCommit.getAuthor());
    //         commit.setDate(EntityUtils.parseDateToLocalDateTimeUTC(ghCommit.getCommitDate()));
    //         commit.setAdditions(ghCommit.getLinesAdded());
    //         commit.setDeletions(ghCommit.getLinesDeleted());
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

}
