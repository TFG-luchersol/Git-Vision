package org.springframework.samples.gitvision.commit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.TimePeriod;
import org.springframework.samples.gitvision.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommitService {

    @Transactional(readOnly = true)
    public List<Commit> getCommitsByRepositoryId(Long repositoryId) throws IOException {
        GitHub gitHub = GitHub.connect();
        GHRepository ghRepository = gitHub.getRepositoryById(repositoryId);
        return ghRepository.listCommits().toList().stream().map(Commit::parse).toList();
    }

    @Transactional(readOnly = true)
    public Map<TimePeriod, Map<Integer, Long>> getNumCommitsGroupByTime(Long repositoryId) throws IOException {
        GitHub github = GitHub.connect();
        GHRepository ghRepository = github.getRepositoryById(repositoryId);
        List<GHCommit> ghCommits = ghRepository.listCommits().toList();
        int minYear = EntityUtils.parseDateToLocalDateUTC(ghCommits.get(0).getCommitDate()).getYear(), 
            maxYear = EntityUtils.parseDateToLocalDateUTC(ghCommits.get(ghCommits.size() - 1).getCommitDate()).getYear();
        Map<TimePeriod, Map<Integer, Long>> cont = new HashMap<>();
        Function<Integer, Long> zero = n -> 0L;
        cont.put(TimePeriod.HOUR, IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toMap(Function.identity(), zero)));
        cont.put(TimePeriod.DAY_OF_WEEK, IntStream.rangeClosed(1, 7).boxed().collect(Collectors.toMap(Function.identity(), zero)));
        cont.put(TimePeriod.MONTH, IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toMap(Function.identity(), zero)));
        cont.put(TimePeriod.YEAR, IntStream.rangeClosed(minYear, maxYear).boxed().collect(Collectors.toMap(Function.identity(), zero)));

        BiConsumer<TimePeriod, Integer> incrementCont = (t, i) -> {
            Map<Integer, Long> innerCont = cont.get(t);
            innerCont.put(i, innerCont.get(i) + 1);
        };
        for (GHCommit ghCommit : ghCommits) {
            LocalDateTime date = EntityUtils.parseDateToLocalDateTimeUTC(ghCommit.getCommitDate());
            incrementCont.accept(TimePeriod.HOUR, date.getHour());
            incrementCont.accept(TimePeriod.DAY_OF_WEEK, date.getDayOfWeek().getValue());
            incrementCont.accept(TimePeriod.MONTH, date.getMonthValue());
            incrementCont.accept(TimePeriod.YEAR, date.getYear());
        }
        return cont;
    }

}
