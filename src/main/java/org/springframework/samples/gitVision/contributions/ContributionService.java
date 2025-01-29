package org.springframework.samples.gitvision.contributions;

import java.util.Date;
import java.util.List;

import org.springframework.samples.gitvision.contributions.model.CommitContribution;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.relations.userRepo.UserRepoRepository;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.samples.gitvision.util.GithubGraphQLApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContributionService {
    
    private final UserRepoRepository userRepoRepository;

    public ContributionService(UserRepoRepository userRepoRepository){
        this.userRepoRepository = userRepoRepository;
    }

    @Transactional(readOnly = true)
    public List<CommitContribution> getContributionsByDateBetweenDates(String repositoryName, String login,
            Date startDate, Date endDate) throws Exception {
        UserRepo userRepo = this.userRepoRepository.findByNameAndUser_Username(repositoryName, login)
            .orElseThrow(() -> new ResourceNotFoundException("Not found repository"));
        String tokenToUse = userRepo.getDecryptedToken();
        GithubGraphQLApi githubGraphQLApi = GithubGraphQLApi.connect(tokenToUse);
        List<CommitContribution> commitContributions = githubGraphQLApi.getContributionsBetweenDates(repositoryName, startDate, endDate);
        return commitContributions;
    }
}
