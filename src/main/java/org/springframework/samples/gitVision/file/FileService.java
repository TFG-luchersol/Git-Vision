package org.springframework.samples.gitvision.file;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTree;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.file.model.ChangesByUser;
import org.springframework.samples.gitvision.file.model.File;
import org.springframework.samples.gitvision.file.model.PercentageLanguages;
import org.springframework.samples.gitvision.file.model.TreeFiles;
import org.springframework.samples.gitvision.file.model.TreeFiles.TreeNode;
import org.springframework.samples.gitvision.relations.userRepo.UserRepoRepository;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepoRepository userRepoRepository;

    public PercentageLanguages getPercentageExtensionsByRespositoryName(String repositoryName, String login) throws IOException{
        String token = null;
        UserRepo userRepo = userRepoRepository.findByNameAndUser_Username(repositoryName, login).get();
        if(userRepo.hasToken()){
            token = userRepo.getToken();
        } else {
            User user = userRepository.findByUsername(login).get();
            token = user.getGithubToken();
        }
        GitHub gitHub = GitHub.connect(login, token);
        GHRepository ghRepository = gitHub.getRepository(repositoryName);
        Map<String, Long> contLanguajes = ghRepository.listLanguages();
        return PercentageLanguages.of(contLanguajes);
    }

    public TreeNode getFileTreeByRepositoryName(String repositoryName, String login) throws IOException{
        String token = null;
        UserRepo userRepo = userRepoRepository.findByNameAndUser_Username(repositoryName, login).get();
        if(userRepo.hasToken()){
            token = userRepo.getToken();
        } else {
            User user = userRepository.findByUsername(login).get();
            token = user.getGithubToken();
        }
        GitHub gitHub = GitHub.connect(login, token);
        GHRepository ghRepository = gitHub.getRepository(repositoryName);
        GHTree ghTree = ghRepository.getTreeRecursive(ghRepository.getDefaultBranch(), -1);
        List<String> paths = ghTree.getTree().stream().map(entry -> entry.getPath()).toList();
        return TreeFiles.buildTreeWithCollapse(paths);
    }

    public Map<String, Long> getExtensionCounterByRepositoryId(String repositoryName, String login) throws IOException{
        String token = null;
        UserRepo userRepo = userRepoRepository.findByNameAndUser_Username(repositoryName, login).get();
        if(userRepo.hasToken()){
            token = userRepo.getToken();
        } else {
            User user = userRepository.findByUsername(login).get();
            token = user.getGithubToken();
        }
        GitHub gitHub = GitHub.connect(login, token);
        GHRepository ghRepository = gitHub.getRepository(repositoryName);
        GHTree ghTree = ghRepository.getTreeRecursive(ghRepository.getDefaultBranch(), -1);
        Map<String, Long> cont = ghTree.getTree().stream()
                               .filter(i -> "blob".equals(i.getType()))
                               .map(entry -> new File(entry.getPath()).getExtension())
                               .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return cont;
    }

    public ChangesByUser getChangeByUserInPath(String repositoryName, String login, String path) throws IOException{
        ChangesByUser changesByUser = new ChangesByUser();
        User user = userRepository.findByUsername(login).get();
        UserRepo userRepo = userRepoRepository.findByNameAndUser_Username(repositoryName, login).get();
        String token = Objects.requireNonNullElse(userRepo.getToken(), user.getGithubToken());
        GitHub github = GitHub.connect(login, token);

        GHRepository repo = github.getRepository(repositoryName);
        
        for (GHCommit ghCommit : repo.queryCommits().path(path).list()) {
            for(var file: ghCommit.listFiles()){

                // Hay que comprobar si un archivo anterior tenía el mismo nombre, 
                // ya que en dicho caso puede suceder que este haya sido renombrado
                if(file.getPreviousFilename() != null && file.getPreviousFilename().equals(path)){
                    innerGetChangeByUserInPath(changesByUser, repo, path);
                    break;
                } else if(path.equals(file.getFileName())){
                    Change change = Change.of(file.getLinesAdded(), file.getLinesDeleted());
                    changesByUser.add(ghCommit.getAuthor().getLogin(), change);
                    break;
                }
            }
        }

        return changesByUser;
    }

    private void innerGetChangeByUserInPath(ChangesByUser changesByUser, GHRepository ghRepository, String path) throws IOException{        
        for (GHCommit ghCommit : ghRepository.queryCommits().path(path).list()) {
            for(GHCommit.File file: ghCommit.listFiles()){
                if(file.getPreviousFilename() != null && file.getPreviousFilename().equals(path)){
                    innerGetChangeByUserInPath(changesByUser, ghRepository, path);
                    break;
                } else if(path.equals(file.getFileName())){
                    Change change = Change.of(file.getLinesAdded(), file.getLinesDeleted());
                    changesByUser.add(ghCommit.getAuthor().getLogin(), change);
                    break;
                }
            }
        }
    }

}
