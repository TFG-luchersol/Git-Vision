package org.springframework.samples.gitvision.contributions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.springframework.samples.gitvision.contributions.model.Contribution;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.relations.repository.GVRepoRepository;
import org.springframework.samples.gitvision.relations.repository.GVRepoUserConfigRepository;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.relations.repository.model.GVRepoUserConfig;
import org.springframework.samples.gitvision.util.GithubGraphQLApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContributionService {

    private final GVRepoRepository gvRepoRepository;
    private final GVRepoUserConfigRepository gvRepoUserConfigurationRepository;

    public ContributionService(GVRepoRepository gvRepoRepository, GVRepoUserConfigRepository gvRepoUserConfigurationRepository){
        this.gvRepoRepository = gvRepoRepository;
        this.gvRepoUserConfigurationRepository = gvRepoUserConfigurationRepository;
    }

    @Transactional(readOnly = true)
    public List<Contribution> getContributionsByDateBetweenDates(String repositoryName, String login, String filePath,
            Date startDate, Date endDate) throws Exception {
        GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Username(repositoryName, login)
            .orElseThrow(() -> new ResourceNotFoundException("Not found repository"));
        String tokenToUse = gvRepo.getToken();
        GithubGraphQLApi githubGraphQLApi = GithubGraphQLApi.connect(tokenToUse);
        List<Contribution> contributions = githubGraphQLApi.getContributionsBetweenDates(repositoryName, filePath, startDate, endDate);
        simplifyContributors(contributions, gvRepo);
        return contributions;
    }


    @Transactional(readOnly = true)
    public List<Contribution> getContributionsInFolderByDateBetweenDates(GHRepository ghRepository, String repositoryName, String login, String path,
            Date startDate, Date endDate) throws Exception {
        GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Username(repositoryName, login)
            .orElseThrow(() -> new ResourceNotFoundException("Not found repository"));
        String tokenToUse = gvRepo.getToken();
        GithubGraphQLApi githubGraphQLApi = GithubGraphQLApi.connect(tokenToUse);
        List<GHContent> contents = ghRepository.getDirectoryContent(path);
        List<Contribution> contributions = contents.parallelStream()
            .map(GHContent::getPath)
            .flatMap(filePath -> {
                try {
                    return githubGraphQLApi.getContributionsBetweenDates(repositoryName, filePath, startDate, endDate).stream();
                } catch (Exception e) {
                    System.err.println("Error obteniendo contribuciones para " + filePath + ": " + e.getMessage());
                    return Stream.empty();
                }
            })
            .collect(Collectors.toList());
        simplifyContributors(contributions, gvRepo);
        return contributions;

    }

    private void simplifyContributors(List<Contribution> contributions, GVRepo gvRepo){
        List<GVRepoUserConfig> alias = gvRepoUserConfigurationRepository.findByGvRepo(gvRepo);

        Map<String, String> resultado = new HashMap<>();
        for (GVRepoUserConfig gvRepoUserConfiguration : alias) {
            resultado.putAll(gvRepoUserConfiguration.toOriginal());
        }

        for (Contribution contribution : contributions) {
            String contributionName = contribution.getAuthorName();
            if(resultado.containsKey(contributionName)) {
                String newContributionName = resultado.get(contributionName);
                contribution.setAuthorName(newContributionName);
            }
        }

    }

}
