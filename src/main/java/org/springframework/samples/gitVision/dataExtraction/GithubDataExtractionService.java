package org.springframework.samples.gitvision.dataExtraction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GHCommit.ShortInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.commit.CommitRepository;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.file.File;
import org.springframework.samples.gitvision.file.FileRepository;
import org.springframework.samples.gitvision.githubUser.GithubUserRepository;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.issue.Issue;
import org.springframework.samples.gitvision.issue.IssueRepository;
import org.springframework.samples.gitvision.relations.collaborator.CollaboratorRepository;
import org.springframework.samples.gitvision.repository.RepoRepository;
import org.springframework.samples.gitvision.repository.Repository;
import org.springframework.samples.gitvision.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class GithubDataExtractionService {

    RepoRepository repoRepository;
    CommitRepository commitRepository;
    IssueRepository issueRepository;
    FileRepository fileRepository;
    CollaboratorRepository collaboratorRepository;
    GithubUserRepository githubUserRepository;

    @Autowired
    public GithubDataExtractionService(RepoRepository repoRepository, CommitRepository commitRepository,
            IssueRepository issueRepository, FileRepository fileRepository,
            CollaboratorRepository collaboratorRepository, GithubUserRepository githubUserRepository) {
        this.repoRepository = repoRepository;
        this.commitRepository = commitRepository;
        this.issueRepository = issueRepository;
        this.fileRepository = fileRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.githubUserRepository = githubUserRepository;
    }

    public void extractRepository(String owner, String repo, String login, String token) {
        String name = String.format("%s/%s", owner, repo);
        try {
            Repository repository = repoRepository.getRepositoryByName(name);
            LocalDateTime updateDate = null;
            Date sinceDate = null;
            if (repository != null) {
                String tokenRepository = repository.getToken();
                if (tokenRepository != null) {
                    token = tokenRepository;
                }
                updateDate = repository.getUpdateDate();
                if(updateDate != null)
                    sinceDate = EntityUtils.parseLocalDateTimeUTCToDate(updateDate);
            }
            GitHub gitHub = GitHub.connect(login, token);
            GHRepository ghRepository = gitHub.getRepository(name);
            extractIssues(ghRepository, sinceDate);
            extractCommits(ghRepository, sinceDate);
            extractCollaborators(ghRepository);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractCommits(GHRepository ghRepository, Date sinceDate) {
        try {
            List<GHCommit> ghCommits = sinceDate == null ? ghRepository.listCommits().toList()
                    : ghRepository.queryCommits().since(sinceDate).list().toList();
            List<Commit> commits = new ArrayList<>();
            for (int i = ghCommits.size() - 1; i >= 0; i--) {
                GHCommit ghCommit = ghCommits.get(i);
                Commit commit = new Commit();
                ShortInfo shortInfo = ghCommit.getCommitShortInfo();
                commit.setId(shortInfo.getNodeId());
                commit.setMessage(shortInfo.getMessage());
                commit.setDate(EntityUtils.parseDateToLocalDateTimeUTC(shortInfo.getCommitDate()));
                commit.setAdditions(ghCommit.getLinesAdded());
                commit.setDeletions(ghCommit.getLinesDeleted());
                commit.setAuthor(null);
                commit.setRepository(null);
                commits.add(commit);
            }
            commitRepository.saveAll(commits);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractIssues(GHRepository ghRepository, Date sinceDate) {
        try {
            List<GHIssue> ghIssues = sinceDate == null ? ghRepository.getIssues(GHIssueState.ALL)
                    : ghRepository.queryIssues().since(sinceDate).list().toList();
            List<Issue> issues = new ArrayList<>();
            for (GHIssue ghIssue : ghIssues) {
                Issue issue = new Issue();
                issue.setId(ghIssue.getId());
                issue.setNumber(ghIssue.getNumber());
                issue.setTitle(ghIssue.getTitle());
                issue.setRepository(null);
                issues.add(issue);
            }
            issueRepository.saveAll(issues);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void extractFiles(GHCommit ghCommit) {
        try {
            Map<String, GHCommit.File> addedFiles = new HashMap<>();
            Map<String, GHCommit.File> removedFiles = new HashMap<>();

            List<GHCommit.File> files = ghCommit.listFiles().toList();

            for (GHCommit.File file : files) {
                String status = file.getStatus();
                if ("added".equals(status))
                    addedFiles.put(file.getFileName(), file);
                if ("removed".equals(status))
                    removedFiles.put(file.getFileName(), file);
            }

            for (Map.Entry<String, GHCommit.File> addedEntry : addedFiles.entrySet()) {
                String addedFilename = addedEntry.getKey();
                GHCommit.File addedFile = addedEntry.getValue();

                for (Map.Entry<String, GHCommit.File> removedEntry : removedFiles.entrySet()) {
                    String removedFilename = removedEntry.getKey();
                    GHCommit.File removedFile = removedEntry.getValue();

                    if (addedFile.getSha().equals(removedFile.getSha())) {
                        File file = fileRepository.getFileByPath(removedFilename);
                        file.setPath(addedFilename);
                        fileRepository.save(file);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractCollaborators(GHRepository ghRepository) {
        try {
            List<GithubUser> oldCollaborators = collaboratorRepository.getAllCollaboratorsByRepositoryId(ghRepository.getId());
            
            for (GHUser ghCollaborator : ghRepository.getCollaborators()) {
                GithubUser collaborator = new GithubUser();
                collaborator.setId(ghCollaborator.getId());
                collaborator.setUsername(ghCollaborator.getLogin());
                collaborator.setAvatarUrl(ghCollaborator.getAvatarUrl());
                collaborator.setEmail(ghCollaborator.getEmail());
                if(!oldCollaborators.contains(collaborator)){

                }
                oldCollaborators.remove(collaborator);
                
            }
            
            // Logica para eliminar a todos los que ya no están relacionados
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
