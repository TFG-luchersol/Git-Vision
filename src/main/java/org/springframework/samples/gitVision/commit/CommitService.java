package org.springframework.samples.gitvision.commit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.kohsuke.github.GHCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.commit.model.CommitsByPerson;
import org.springframework.samples.gitvision.commit.model.CommitsByTimePeriod;
import org.springframework.samples.gitvision.commit.model.TimePeriod;
import org.springframework.samples.gitvision.util.EntityUtils;
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
    public List<CommitsByPerson> getNumCommitsByUserInPeriod(Long repositoryId, LocalDateTime startDate,
            LocalDateTime endDate) {
        if (startDate == null && endDate == null)
            return this.commitRepository.getNumCommitsByUserOnDate(repositoryId, startDate, endDate);
        else if (startDate == null)
            return this.commitRepository.getNumCommitsByUserBeforeThat(repositoryId, endDate);
        else if (endDate == null)
            return this.commitRepository.getNumCommitsByUserAfterThat(repositoryId, startDate);
        else
            return this.commitRepository.getNumCommitsByUser(repositoryId);
    }

    @Transactional(readOnly = true)
    public List<CommitsByTimePeriod> getNumCommitsGroupByTime(Long repositoryId, TimePeriod timePeriod) {
        List<Integer[]> res = switch (timePeriod) {
            case HOUR -> commitRepository.getNumCommitsByHour(repositoryId);
            case DAY_OF_WEEK -> commitRepository.getNumCommitsByDayOfWeek(repositoryId);
            case MONTH -> commitRepository.getNumCommitsByMonth(repositoryId);
            case YEAR -> commitRepository.getNumCommitsByYear(repositoryId);
        };
        return CommitsByTimePeriod.of(res, timePeriod);
    }

    @Transactional
    public void saveCommit(GHCommit ghCommit) {
        Commit commit = new Commit();
        try {
            commit.setMessage(ghCommit.getCommitShortInfo().getMessage());
            // commit.setAuthor(ghCommit.getAuthor());
            commit.setDate(EntityUtils.parseDateToLocalDateTimeUTC(ghCommit.getCommitDate()));
            commit.setAdditions(ghCommit.getLinesAdded());
            commit.setDeletions(ghCommit.getLinesDeleted());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
