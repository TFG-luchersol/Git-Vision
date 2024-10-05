package org.springframework.samples.gitvision.file;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTree;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
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
        GHTree ghTree = ghRepository.getTreeRecursive("main", -1);
        List<File> files = ghTree.getTree().stream().map(entry -> new File(entry.getPath())).toList();
        return TreeFiles.buildTreeFromFilesWithCollapse(files);
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
        GHTree ghTree = ghRepository.getTreeRecursive("main", -1);
        Map<String, Long> cont = ghTree.getTree().stream()
                               .map(entry -> new File(entry.getPath()).getExtension())
                               .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return cont;
    }

}
