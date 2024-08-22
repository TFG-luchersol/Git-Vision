package org.springframework.samples.gitvision.dataExtraction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.change.ChangesRepository;
import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.commit.CommitRepository;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.file.FileRepository;
import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.githubUser.GithubUserRepository;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.issue.Issue;
import org.springframework.samples.gitvision.issue.IssueRepository;
import org.springframework.samples.gitvision.relations.collaborator.CollaboratorRepository;
import org.springframework.samples.gitvision.relations.issueCommit.IssueCommit;
import org.springframework.samples.gitvision.relations.issueCommit.IssueCommitRepository;
import org.springframework.samples.gitvision.repository.RepoRepository;
import org.springframework.samples.gitvision.repository.Repository;
import org.springframework.samples.gitvision.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GithubDataExtractionService {

    RepoRepository repoRepository;
    CommitRepository commitRepository;
    IssueRepository issueRepository;
    FileRepository fileRepository;
    CollaboratorRepository collaboratorRepository;
    GithubUserRepository githubUserRepository;
    IssueCommitRepository issueCommitRepository;
    ChangesRepository changesRepository;

    @Autowired
    public GithubDataExtractionService(RepoRepository repoRepository, CommitRepository commitRepository,
            IssueRepository issueRepository, FileRepository fileRepository,
            CollaboratorRepository collaboratorRepository, GithubUserRepository githubUserRepository,
            IssueCommitRepository issueCommitRepository, ChangesRepository changesRepository) {
        this.repoRepository = repoRepository;
        this.commitRepository = commitRepository;
        this.issueRepository = issueRepository;
        this.fileRepository = fileRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.githubUserRepository = githubUserRepository;
        this.issueCommitRepository = issueCommitRepository;
        this.changesRepository = changesRepository;
    }

    @Transactional
    public void extractRepository(String owner, String repo, String login, String token) {
        String name = String.format("%s/%s", owner, repo);
        try {
            GitHub gitHub = GitHub.connect(login, token);
            GHRepository ghRepository = gitHub.getRepository(name);
            Repository repository = repoRepository.findByName(name);
            LocalDateTime updateDate = null;
            Date sinceDate = null;
            if (repository == null) {
                repository = new Repository();
                repository.setId(ghRepository.getId());
                repository.setName(name);
                repository.setToken(token);
                repository.setUpdateDate(LocalDateTime.now());
                repoRepository.save(repository);
            } else {
                String tokenRepository = repository.getToken();
                if (tokenRepository != null)
                    token = tokenRepository;
                updateDate = repository.getUpdateDate();
                if (updateDate != null)
                    sinceDate = EntityUtils.parseLocalDateTimeUTCToDate(updateDate);
            }

            extractIssues(ghRepository, repository, sinceDate);
            extractCommits(ghRepository, repository, sinceDate);
            // extractCollaborators(ghRepository, repository);
            // repoRepository.save(repository);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    private void extractIssues(GHRepository ghRepository, Repository repository, Date sinceDate) {
        try {
            List<GHIssue> ghIssues = sinceDate == null ? ghRepository.getIssues(GHIssueState.ALL)
                    : ghRepository.queryIssues().since(sinceDate).list().toList();
            for (GHIssue ghIssue : ghIssues) {
                Issue issue = new Issue();
                issue.setId(ghIssue.getId());
                issue.setNumber(ghIssue.getNumber());
                issue.setTitle(ghIssue.getTitle());
                issue.setRepository(repository);
                issueRepository.save(issue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    private void extractCommits(GHRepository ghRepository, Repository repository, Date sinceDate) {
        try {
            List<GHCommit> ghCommits = sinceDate == null ? ghRepository.listCommits().toList()
                    : ghRepository.queryCommits().since(sinceDate).list().toList();
            for (int i = ghCommits.size() - 1; i >= 0; i--) {
                Commit commit = new Commit();
                GithubUser author;
                GHCommit ghCommit = ghCommits.get(i);
                String commitId = repository.getName() + "/" + ghCommit.getSHA1();
                String usernameAuthor = ghCommit.getAuthor().getLogin();
                Optional<GithubUser> optionalAuthor = githubUserRepository.findByUsername(usernameAuthor);
                commit.setId(commitId);
                commit.setMessage(ghCommit.getCommitShortInfo().getMessage());
                commit.setDate(EntityUtils.parseDateToLocalDateTimeUTC(ghCommit.getCommitDate()));
                commit.setAdditions(ghCommit.getLinesAdded());
                commit.setDeletions(ghCommit.getLinesDeleted());
                commit.setRepository(repository);
                if (optionalAuthor.isPresent()) {
                    author = optionalAuthor.get();
                } else {
                    author = new GithubUser();
                    GHUser ghAuthor = ghCommit.getAuthor();
                    author.setId(ghAuthor.getId());
                    author.setUsername(ghAuthor.getLogin());
                    author.setAvatarUrl(ghAuthor.getAvatarUrl());
                    author.setEmail(ghAuthor.getEmail());
                    githubUserRepository.save(author);
                }
                commit.setAuthor(author);
                commitRepository.save(commit);
                for (Integer number : commit.getIssueNumbers()) {
                    Optional<Issue> issue = issueRepository.findIssueByNumberAndRepository_Id(number, repository.getId());
                    if (issue.isPresent()) {
                        Commit savedCommit = commitRepository.findById(commitId).get();
                        IssueCommit issueCommit = new IssueCommit();
                        issueCommit.setCommit(savedCommit);
                        issueCommit.setIssue(issue.get());
                        issueCommitRepository.save(issueCommit);
                    }
                }
                extractFiles(ghCommit, author.getId(), repository.getId(), i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Transactional
    private void extractFiles(GHCommit ghCommit, Long authorId, Long repositoryId, int i2) {
        try {
            // Obtenemos todos los archivos
            Repository repository = repoRepository.findById(repositoryId).get();
            GithubUser author = githubUserRepository.findById(authorId).get();
            List<GHCommit.File> files = ghCommit.listFiles().toList();

            // Según el status guardamos los archivos añadidos o borrados,
            // mientras que los renombrados y modificados se tratan de forma directan .
            // En caso de tratarse de un estado diferente saltará una excepción.
            for (int i = 0; i < files.size(); i++) {
                GHCommit.File file = files.get(i);
                String path = file.getFileName();
                String status = file.getStatus();

                switch (status) {
                    case "added" -> { 
                        // En ocasiones puede ocurrir que el mismo archivo se introduzca en diferentes commits.
                        // En ese caso, pasamos al siguiente archivo.
                        if(fileRepository.existsByPathAndRepository_Id(path, repositoryId))
                            continue;
                        File newFile = new File();
                        newFile.setPath(path);
                        newFile.setRepository(repository);
                        fileRepository.save(newFile);
                    }
                    case "removed" -> {
                        Optional<File> deletedFile = fileRepository.findByPathAndRepository_Id(path, repositoryId);
                        if(deletedFile.isPresent()){
                            fileRepository.delete(deletedFile.get());
                        }
                    }
                    case "modified" -> {
                        Optional<File> optionalUpdatedFile = fileRepository.findByPathAndRepository_Id(path, repositoryId);
                        if(optionalUpdatedFile.isPresent()){
                            File updatedFile = optionalUpdatedFile.get();
                            Change change = new Change();
                            change.setAuthor(author);
                            change.setFile(updatedFile);
                            change.setAdditions(file.getLinesAdded());
                            change.setDeletions(file.getLinesDeleted());
                            updatedFile.setPath(file.getFileName());
                            changesRepository.save(change);
                            fileRepository.save(updatedFile);
                        }
                    }
                    case "renamed" -> {
                        String previousPath = file.getPreviousFilename();
                        Optional<File> optionalUpdatedFile = fileRepository.findByPathAndRepository_Id(previousPath, repositoryId);
                        if(optionalUpdatedFile.isPresent()){
                            File updatedFile = optionalUpdatedFile.get();
                            updatedFile.setPath(file.getFileName());
                            // Aunque sea marcado como renombrado también está la posibilidad de que haya
                            // sido modificado. Antes de seguir construyendo 
                            Change change = new Change();
                            change.setAdditions(file.getLinesAdded());
                            change.setDeletions(file.getLinesDeleted());
                            if (change.withChanges()) {
                                change.setAuthor(author);
                                change.setFile(updatedFile);
                                changesRepository.save(change);
                            }
                            fileRepository.save(updatedFile);
                        }
                        
                    }
                    default -> throw new Error("Not found status:" + status);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    private void extractCollaborators(GHRepository ghRepository, Repository repository) {
        try {
            List<GithubUser> oldCollaborators = collaboratorRepository
                    .findAllCollaboratorByRepository_Id(ghRepository.getId());

            for (GHUser ghCollaborator : ghRepository.getCollaborators()) {
                GithubUser collaborator = new GithubUser();
                collaborator.setId(ghCollaborator.getId());
                collaborator.setUsername(ghCollaborator.getLogin());
                collaborator.setAvatarUrl(ghCollaborator.getAvatarUrl());
                collaborator.setEmail(ghCollaborator.getEmail());
                if (!oldCollaborators.contains(collaborator)) {

                }
                oldCollaborators.remove(collaborator);

            }

            // Logica para eliminar a todos los que ya no están relacionados
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
