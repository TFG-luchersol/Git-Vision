package org.springframework.samples.gitvision.relations.userRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRepoService {
    
    private final UserRepoRepository userRepoRepository;
    private final UserRepository userRepository;

    public UserRepoService(UserRepoRepository userRepoRepository, UserRepository userRepository){
        this.userRepoRepository = userRepoRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, List<String>> getAllRepositories(Long userId) {
        List<String> nameRepositories = this.userRepoRepository.findAllRepository_NameByUser_Id(userId);
        Map<String, List<String>> dict = new HashMap<>();
        for (String name : nameRepositories) {
            String[] pieces = name.split("/");
            String owner = pieces[0], repo = pieces[1];
            if(dict.containsKey(owner)){
                dict.get(owner).add(repo);
            } else {
                List<String> ls = new ArrayList<>();
                ls.add(repo);
                dict.put(owner, ls);
            }
        }
        return dict;
    }

    @Transactional(readOnly = true)
    public List<String> getAllOwnersByUserId(Long userId) {
        List<String> nameRepositories = this.userRepoRepository.findAllRepository_NameByUser_Id(userId);
        List<String> owners = nameRepositories.stream().map(i -> i.split("/")[0]).toList();
        return owners;
    }

    @Transactional(readOnly = true)
    public GitHub connect(String repositoryName, String login) throws Exception {
        UserRepo userRepo = this.userRepoRepository.findByNameAndUser_Username(repositoryName, login).get();
        String tokenToUse = userRepo.getDecryptedToken();
        return GitHub.connect(login, tokenToUse);
    }

    @Transactional(readOnly = true)
    public GHRepository getRepository(String owner, String repo, String login) throws Exception {
        String repositoryName = new StringBuilder().append(owner).append("/").append(repo).toString();
        GitHub github = connect(repositoryName, login);
        return github.getRepository(repositoryName);
    }

    @Transactional(readOnly = true)
    public GHRepository getRepository(String repositoryName, String login) throws Exception {
        GitHub github = connect(repositoryName, login);
        return github.getRepository(repositoryName);
    }

    @Transactional(readOnly = true)
    public List<GithubUser> getContributors(GHRepository ghRepository) throws Exception {
        return ghRepository.listContributors().toList().stream().map(GithubUser::parseContributor).toList();
    }

    @Transactional
    public void updateGithubToken(String repositoryName, String login, String newGithubToken) throws Exception {
        UserRepo userRepo = this.userRepoRepository.findByNameAndUser_Username(repositoryName, login).orElseThrow(() -> new ResourceNotFoundException("Not found repository"));   
        GitHub github = GitHub.connect(login, newGithubToken);
        if(github.getMyself() == null){
            throw new IllegalAccessException("Token invalido");
        }
        userRepo.setTokenAndEncrypt(newGithubToken);
        this.userRepoRepository.save(userRepo);
    }

    @Transactional
    public void linkUserWithRepository(String login, String repositoryName, String token){
        try {
            User user = this.userRepository.findByUsername(login).get();
            String tokenToUse = Objects.requireNonNullElse(token, user.getDecryptedGithubToken());
            
            GitHub gitHub = GitHub.connect(login, tokenToUse);
            GHRepository ghRepository = gitHub.getRepository(repositoryName);
            UserRepo userRepo = new UserRepo();
            userRepo.setName(repositoryName);
            userRepo.setRepositoryId(ghRepository.getId());
            userRepo.setTokenAndEncrypt(tokenToUse);
            userRepo.setUser(user);
            userRepoRepository.save(userRepo);
        } catch (Exception e) {
            
        }
        
    }

}
