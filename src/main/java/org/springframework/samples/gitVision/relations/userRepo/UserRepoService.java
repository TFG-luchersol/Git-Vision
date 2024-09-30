package org.springframework.samples.gitvision.relations.userRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.util.Credential;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRepoService {
    
    @Autowired
    UserRepoRepository userRepoRepository;

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

    @Transactional
    public void linkUserWithRepository(Credential credential, String repositoryName, String token, User user){
        try {
            GitHub gitHub = GitHub.connect(credential.getLogin(), credential.getOauthAccessToken());
            GHRepository ghRepository = gitHub.getRepository(repositoryName);
            UserRepo userRepo = new UserRepo();
            userRepo.setName(repositoryName);
            userRepo.setRepositoryId(ghRepository.getId());
            userRepo.setToken(token);
            userRepo.setUser(user);
            userRepoRepository.save(userRepo);
        } catch (Exception e) {
            
        }
        
    }

}
