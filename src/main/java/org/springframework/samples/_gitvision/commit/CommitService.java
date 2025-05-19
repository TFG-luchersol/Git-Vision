package org.springframework.samples.gitvision.commit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.repository.GVRepoRepository;
import org.springframework.samples.gitvision.relations.repository.GVRepoUserConfigRepository;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.relations.repository.model.GVRepoUserConfig;
import org.springframework.samples.gitvision.util.GithubApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommitService {

    private final GVRepoRepository gvRepoRepository;
    private final GVRepoUserConfigRepository gvRepoUserConfigRepository;

    public CommitService(GVRepoRepository gvRepoRepository, GVRepoUserConfigRepository gvRepoUserConfigRepository){
        this.gvRepoRepository = gvRepoRepository;
        this.gvRepoUserConfigRepository = gvRepoUserConfigRepository;
    }

    @Transactional(readOnly = true)
    public List<Commit> getCommitsByRepository(String repositoryName, String login, Integer page)  {
        GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Username(repositoryName, login).get();
        String tokenToUse = gvRepo.getToken();
        return GithubApi.connect(repositoryName, tokenToUse)
                        .getCommitsByPage(page, 30);
    }

    @Transactional(readOnly = true)
    public List<Commit> getCommitsByRepositoryWithFilter(String repositoryName, String login, String filter, boolean isRegex, boolean isOwner) throws IOException  {
        GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Username(repositoryName, login)
            .orElseThrow(() -> ResourceNotFoundException.of(GVRepo.class));
        String tokenToUse = gvRepo.getToken();
        GitHub gitHub = new GitHubBuilder().withOAuthToken(tokenToUse).build();

        Stream<GHCommit> ghCommits;

        if (isOwner) {
            GVRepoUserConfig gvRepoUserConfig = gvRepoUserConfigRepository.findByGvRepoAndUsername(gvRepo, filter).orElse(null);
            if(gvRepoUserConfig == null) {
                ghCommits = gitHub.getRepository(repositoryName)
                    .queryCommits()
                    .author(filter).list().toList().stream();
            } else {
                List<GHCommit> auxGhCommits = new ArrayList<>();
                for (String alias : gvRepoUserConfig.getAllOptions()) {
                    auxGhCommits.addAll(gitHub.getRepository(repositoryName)
                        .queryCommits()
                        .author(alias)
                        .list().toList());
                }
                ghCommits = auxGhCommits.stream();
            }

        } else if (isRegex) {
            try {
                Pattern pattern = Pattern.compile(filter);
                ghCommits = gitHub.getRepository(repositoryName)
                    .listCommits()
                    .toList()
                    .stream()
                    .filter(commit -> {
                        try {
                            String message = commit.getCommitShortInfo().getMessage();
                            return message != null && pattern.matcher(message).find();
                        } catch (IOException e) {
                            return false;
                        }
                    });
            } catch (PatternSyntaxException e) {
                ghCommits = Stream.empty();
            }

        } else {
            ghCommits = gitHub.searchCommits()
                .repo(repositoryName)
                .q(filter)
                .list()
                .toList()
                .stream();
        }

        return ghCommits
                .map(Commit::parse)
                .toList();
    }


    @Transactional(readOnly = true)
    public Commit getCommitByRepositoryNameAndSha(GHRepository ghRepository, String sha) throws IOException {
        GHCommit ghCommit = ghRepository.getCommit(sha);
        Commit commit = Commit.parse(ghCommit);
        for (Integer issueNumber : commit.getIssueNumbers()) {
            try {
                GHIssue ghIssue = ghRepository.getIssue(issueNumber);
                commit.getIssues().add(Issue.parse(ghIssue));
            } catch (Exception e) {

            }
        }
        return commit;
    }


}
