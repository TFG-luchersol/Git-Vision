package org.springframework.samples.gitvision.dataExtraction;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.h2.util.Utils;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.change.ChangesRepository;
import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.commit.CommitRepository;
import org.springframework.samples.gitvision.commit.model.Commit;
import org.springframework.samples.gitvision.exceptions.ExtractionException;
import org.springframework.samples.gitvision.exceptions.ExtractionException.TypeExtraction;
import org.springframework.samples.gitvision.file.FileRepository;
import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.githubUser.GithubUserRepository;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.issue.Issue;
import org.springframework.samples.gitvision.issue.IssueRepository;
import org.springframework.samples.gitvision.relations.collaborator.CollaboratorRepository;
import org.springframework.samples.gitvision.relations.collaborator.model.Collaborator;
import org.springframework.samples.gitvision.relations.issueCommit.IssueCommit;
import org.springframework.samples.gitvision.relations.issueCommit.IssueCommitRepository;
import org.springframework.samples.gitvision.relations.userRepo.UserRepoRepository;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.repository.RepoRepository;
import org.springframework.samples.gitvision.repository.model.Repository;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.samples.gitvision.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
// @RequiredArgsConstructor
public class GithubDataExtractionService {

    RepoRepository repoRepository;
    CommitRepository commitRepository;
    IssueRepository issueRepository;
    FileRepository fileRepository;
    CollaboratorRepository collaboratorRepository;
    GithubUserRepository githubUserRepository;
    IssueCommitRepository issueCommitRepository;
    ChangesRepository changesRepository;
    UserRepoRepository userRepoRepository;
    UserRepository userRepository;

    
    @Autowired
    public GithubDataExtractionService(RepoRepository repoRepository, CommitRepository commitRepository,
            IssueRepository issueRepository, FileRepository fileRepository,
            CollaboratorRepository collaboratorRepository, GithubUserRepository githubUserRepository,
            IssueCommitRepository issueCommitRepository, ChangesRepository changesRepository,
            UserRepoRepository userRepoRepository, UserRepository userRepository) {
        this.repoRepository = repoRepository;
        this.commitRepository = commitRepository;
        this.issueRepository = issueRepository;
        this.fileRepository = fileRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.githubUserRepository = githubUserRepository;
        this.issueCommitRepository = issueCommitRepository;
        this.changesRepository = changesRepository;
        this.userRepoRepository = userRepoRepository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void extractRepository(String owner, String repo, String login, String token) throws Exception{
        String name = String.format("%s/%s", owner, repo);
        try {
            GitHub gitHub = GitHub.connect(login, token);
            GHRepository ghRepository = gitHub.getRepository(name);
            Repository repository = repoRepository.findByName(name).orElse(null);
            LocalDateTime updateDate = null;
            Date sinceDate = null;
            if (repository == null) {
                repository = new Repository();
                repository.setId(ghRepository.getId());
                repository.setName(name);
                repository.setToken(token);
            } else {
                String tokenRepository = repository.getToken();
                if (tokenRepository != null)
                    token = tokenRepository;
                updateDate = repository.getUpdateDate();
                if (updateDate != null)
                    sinceDate = EntityUtils.parseLocalDateTimeUTCToDate(updateDate);
            }

            repository.setUpdateDate(LocalDateTime.now());
            repoRepository.save(repository);

            extractIssues(ghRepository, repository, sinceDate);
            extractCommits(ghRepository, repository, sinceDate);
            // extractCollaborators(ghRepository, repository);
        } catch (Exception e) {
            if(e instanceof ExtractionException)
                throw e;
            throw new ExtractionException(TypeExtraction.GITHUB, "REPOSITORY");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void extractIssues(GHRepository ghRepository, Repository repository, Date sinceDate) throws Exception {
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
        } catch (Exception e) {
            if(e instanceof ExtractionException)
                throw e;
            throw new ExtractionException(TypeExtraction.GITHUB, "ISSUE");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    private void extractCommits(GHRepository ghRepository, Repository repository, Date sinceDate) throws Exception {
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
                String message = ghCommit.getCommitShortInfo().getMessage();
                if(message.length() > 255)
                    message = message.substring(0, 252) + "...";
                commit.setId(commitId);
                commit.setMessage(message);
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
                // extractFiles(ghCommit, author.getId(), repository.getId(), i);
            }
        } catch (Exception e) {
            if(e instanceof ExtractionException)
                throw e;
            throw new ExtractionException(TypeExtraction.GITHUB, "COMMIT");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    private void extractFiles(GHCommit ghCommit, Long authorId, Long repositoryId, int i2) throws Exception {
        try {
            // Obtenemos todos los archivos
            Repository repository = repoRepository.findById(repositoryId).get();
            GithubUser author = githubUserRepository.findById(authorId).get();
            List<GHCommit.File> files = ghCommit.listFiles().toList();

            // Según el status guardamos los archivos añadidos o borrados,
            // mientras que los renombrados y modificados se tratan de forma directan .
            // En caso de tratarse de un estado diferente saltará una excepción.
            if(i2 == 47){
                int b = 3;
            }
            for (int i = 0; i < files.size(); i++) {
                GHCommit.File file = files.get(i);
                String path = file.getFileName();
                String status = file.getStatus();
                if(i2 == 592){
                    int b = 4;
                }
                switch (status) {
                    case "added" -> { 
                        // En ocasiones puede ocurrir que el mismo archivo se introduzca en diferentes commits.
                        // En ese caso, pasamos al siguiente archivo.
                        if(fileRepository.findByPathAndRepository_Id(path, repositoryId).isPresent())
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

        } catch (Exception e) {
            if(e instanceof ExtractionException)
                throw e;
            throw new ExtractionException(TypeExtraction.GITHUB, "FILE of commit "+i2);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void extractCollaborators(GHRepository ghRepository, Repository repository) throws Exception {
        try {
            Set<GithubUser> oldCollaborators = new HashSet<>(collaboratorRepository
                    .findAllCollaboratorByRepository_Id(repository.getId()));

            Set<GithubUser> newCollaborators = new HashSet<>();
            
            for (GHUser ghCollaborator : ghRepository.getCollaborators()) {
                GithubUser collaborator = new GithubUser();
                collaborator.setId(ghCollaborator.getId());
                collaborator.setUsername(ghCollaborator.getLogin());
                collaborator.setAvatarUrl(ghCollaborator.getAvatarUrl());
                collaborator.setEmail(ghCollaborator.getEmail());
                newCollaborators.add(collaborator);
            }

            for (GithubUser githubUser : newCollaborators) {
                Optional<GithubUser> optUpdatedGithubUser = githubUserRepository.findById(githubUser.getId());
                if(optUpdatedGithubUser.isPresent()){
                    GithubUser updatedGithubUser = optUpdatedGithubUser.get();
                    BeanUtils.copyProperties(githubUser, updatedGithubUser);
                    githubUserRepository.save(updatedGithubUser);
                }
            }

            // Los usuarios que sean removidos del repositorio se borrarán sus relaciones a este
            Set<GithubUser> removedUser = new HashSet<>(oldCollaborators);
            removedUser.removeAll(newCollaborators);

            // Eliminamos la relación de colaboración entre githubUser y repository
            for (GithubUser githubUser : removedUser) {
                Optional<Collaborator> collaborator = collaboratorRepository.findByCollaborator_IdAndRepository_Id(githubUser.getId(), repository.getId());
                if(collaborator.isPresent())
                    collaboratorRepository.delete(collaborator.get());
            }
        
        } catch (Exception e) {
            if(e instanceof ExtractionException)
                throw e;
            throw new ExtractionException(TypeExtraction.GITHUB, "COLLABORATOR");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void linkRepositoryToUser(Long repositoryId, Long userId){
        UserRepo userRepo = new UserRepo();
        User user = userRepository.findById(userId).get();
        Repository repository = repoRepository.findById(repositoryId).get();
        userRepo.setUser(user);
        userRepo.setRepository(repository);
        userRepoRepository.save(userRepo);
    }

}
