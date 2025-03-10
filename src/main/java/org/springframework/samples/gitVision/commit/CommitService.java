package org.springframework.samples.gitvision.commit;

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
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.TimePeriod;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.repository.GVRepoRepository;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.util.EntityUtils;
import org.springframework.samples.gitvision.util.GithubApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommitService {

    private final GVRepoRepository gvRepoRepository;

    public CommitService(GVRepoRepository gvRepoRepository){
        this.gvRepoRepository = gvRepoRepository;
    }

    @Transactional(readOnly = true)
    public List<Commit> getCommitsByRepository(String repositoryName, String login, Integer page) throws Exception {
        GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Username(repositoryName, login).get();
        String tokenToUse = gvRepo.getToken();
        return GithubApi.getCommitsByPage(repositoryName, page, 30, tokenToUse);
    }

    @Transactional(readOnly = true)
    public Commit getCommitByRepositoryNameAndSha(GHRepository ghRepository, String sha) throws Exception {
        GHCommit ghCommit = ghRepository.getCommit(sha);
        Commit commit = Commit.parse(ghCommit);
        for (Integer issueNumber : commit.getIssueNumbers()) {
            commit.getIssues().add(Issue.parse(ghRepository.getIssue(issueNumber)));
        }
        return commit;
    }

    @Transactional(readOnly = true)
    public Map<TimePeriod, Map<Integer, Long>> getNumCommitsGroupByTime(GHRepository ghRepository)
            throws Exception {
        List<GHCommit> ghCommits = ghRepository.listCommits().toList();
        int minYear = EntityUtils.parseDateToLocalDateUTC(ghCommits.get(0).getCommitDate()).getYear(),
                maxYear = EntityUtils.parseDateToLocalDateUTC(ghCommits.get(ghCommits.size() - 1).getCommitDate())
                        .getYear();
        Map<TimePeriod, Map<Integer, Long>> cont = new HashMap<>();
        Function<Integer, Long> zero = _ -> 0L;
        BiFunction<Integer, Integer, Map<Integer, Long>> empty = (start, end) -> IntStream.rangeClosed(start, end)
                .boxed().collect(Collectors.toMap(Function.identity(), zero));
        cont.put(TimePeriod.HOUR, empty.apply(0, 23));
        cont.put(TimePeriod.DAY_OF_WEEK, empty.apply(1, 7));
        cont.put(TimePeriod.MONTH, empty.apply(1, 12));
        cont.put(TimePeriod.YEAR, empty.apply(minYear, maxYear));

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
