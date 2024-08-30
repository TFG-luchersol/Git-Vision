package org.springframework.samples.gitvision.relations.userRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.githubUser.GithubUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRepoService {
    
    UserRepoRepository userRepoRepository;

    @Autowired
    public UserRepoService(UserRepoRepository userRepoRepository) {
        this.userRepoRepository = userRepoRepository;
    }

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

    public List<String> getAllOwnersByUserId(Long userId) {
        List<String> nameRepositories = this.userRepoRepository.findAllRepository_NameByUser_Id(userId);
        List<String> owners = nameRepositories.stream().map(i -> i.split("/")[0]).toList();
        return owners;
    }

}
