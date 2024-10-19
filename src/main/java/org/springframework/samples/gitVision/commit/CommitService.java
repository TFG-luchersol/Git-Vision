package org.springframework.samples.gitvision.commit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.commit.model.commitsByTimePeriod.TimePeriod;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.userRepo.UserRepoRepository;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.samples.gitvision.util.EntityUtils;
import org.springframework.samples.gitvision.util.GithubApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommitService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRepoRepository userRepoRepository;

    @Transactional(readOnly = true)
    public List<Commit> getCommitsByRepository(String repositoryName, String login, Integer page) throws IOException {
        User user = this.userRepository.findByUsername(login).get();
        UserRepo userRepo = this.userRepoRepository.findByNameAndUser_Id(repositoryName, user.getId()).get();
        String tokenToUse = Objects.requireNonNullElse(userRepo.getToken(), user.getGithubToken());
        GitHub gitHub = GitHub.connect(login, tokenToUse);
        GHRepository ghRepository = gitHub.getRepository(repositoryName);
        PagedIterator<GHCommit> commits = ghRepository.queryCommits().pageSize(10).list().iterator();
        List<GHCommit> ghCommits = new ArrayList<>();
        for (int i = 0; i < page; i++) {
            ghCommits = commits.nextPage();
        }
        return ghCommits.stream().map(Commit::parse).toList();
    }

    @Transactional(readOnly = true)
    public Commit getCommitByRepositoryNameAndSha(String repositoryName, String sha, String login) throws IOException {
        User user = this.userRepository.findByUsername(login).orElse(null);
        UserRepo userRepo = this.userRepoRepository.findByNameAndUser_Id(repositoryName, user.getId()).get();
        String tokenToUse = Objects.requireNonNullElse(userRepo.getToken(), user.getGithubToken());
        GitHub gitHub = GitHub.connect(login, tokenToUse);
        GHRepository ghRepository = gitHub.getRepository(repositoryName);
        GHCommit ghCommit = ghRepository.getCommit(sha);
        Commit commit = Commit.parse(ghCommit);
        for (Integer issueNumber : commit.getIssueNumbers()) {
            commit.getIssues().add(Issue.parse(ghRepository.getIssue(issueNumber)));
        }
        return commit;
    }

    @Transactional(readOnly = true)
    public Map<TimePeriod, Map<Integer, Long>> getNumCommitsGroupByTime(String repositoryName, String login) throws IOException {
        User user = this.userRepository.findByUsername(login).get();
        UserRepo userRepo = this.userRepoRepository.findByNameAndUser_Id(repositoryName, user.getId()).get();
        String tokenToUse = Objects.requireNonNullElse(userRepo.getToken(), user.getGithubToken());
        GitHub gitHub = GitHub.connect(login, tokenToUse);
        GHRepository ghRepository = gitHub.getRepository(repositoryName);
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
