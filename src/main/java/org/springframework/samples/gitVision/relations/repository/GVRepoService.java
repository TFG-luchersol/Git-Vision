package org.springframework.samples.gitvision.relations.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepository.Contributor;
import org.kohsuke.github.GitHub;
import org.springframework.samples.gitvision.exceptions.ConnectionGithubException;
import org.springframework.samples.gitvision.exceptions.LinkedException;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.relations.repository.model.AliasesDTO;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.relations.repository.model.GVRepoUserConfiguration;
import org.springframework.samples.gitvision.relations.workspace.GVWorkspaceRepository;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.samples.gitvision.user.GVUser;
import org.springframework.samples.gitvision.user.GVUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GVRepoService {

    private final GVRepoRepository gvRepoRepository;
    private final GVWorkspaceRepository gvWorkspaceRepository;
    private final GVUserRepository gvUserRepository;
    private final GVRepoUserConfigurationRepository gvRepoUserConfigurationRepository;

    public GVRepoService(GVRepoRepository gvRepoRepository,
            GVWorkspaceRepository gvWorkspaceRepository,
            GVUserRepository gvUserRepository,
            GVRepoUserConfigurationRepository gvRepoUserConfigurationRepository) {
        this.gvRepoRepository = gvRepoRepository;
        this.gvWorkspaceRepository = gvWorkspaceRepository;
        this.gvUserRepository = gvUserRepository;
        this.gvRepoUserConfigurationRepository = gvRepoUserConfigurationRepository;
    }

    @Transactional(readOnly = true)
    public Map<String, List<String>> getAllRepositories(Long userId) {
        var nameRepositories = this.gvRepoRepository.findNamesByUser_Id(userId);
        Map<String, List<String>> dict = new HashMap<>();
        for (String name : nameRepositories) {
            String[] pieces = name.split("/");
            String owner = pieces[0], repo = pieces[1];
            if (dict.containsKey(owner)) {
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
        List<String> nameRepositories = this.gvRepoRepository.findNamesByUser_Id(userId);
        List<String> owners = nameRepositories.stream().map(i -> i.split("/")[0]).toList();
        return owners;
    }

    @Transactional(readOnly = true)
    public GitHub connect(String repositoryName, String login) throws ConnectionGithubException {
        try {
            GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Username(repositoryName, login).get();
            String tokenToUse = gvRepo.getToken();
            return GitHub.connect(login, tokenToUse);
        } catch (Exception e) {
            throw new ConnectionGithubException(e.getMessage());
        }

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

    public List<GVRepoUserConfiguration> getRepositoryConfiguration(String repositoryName, Long userId) {
        List<GVRepoUserConfiguration> gvRepoUserConfigurations = this.gvRepoUserConfigurationRepository
                .findByGvRepo_NameAndGvRepo_User_Id(repositoryName, userId);
        return gvRepoUserConfigurations;
    }

    public GVRepoUserConfiguration updateAliaUserConfigurations(String repositoryName, Long userId, AliasesDTO aliasesDTO) {
        GVRepoUserConfiguration gvRepoUserConfiguration = this.gvRepoUserConfigurationRepository
                .findByGvRepo_NameAndGvRepo_User_IdAndUsername(repositoryName, userId, aliasesDTO.username())
                .orElseThrow(() -> ResourceNotFoundException.of(GVRepoUserConfiguration.class));
        if(!aliasesDTO.aliases().equals(gvRepoUserConfiguration.getAliases())) {
            gvRepoUserConfiguration.setAliases(aliasesDTO.aliases());
            gvRepoUserConfigurationRepository.save(gvRepoUserConfiguration);
        }

        return gvRepoUserConfiguration;
    }

    public Map<String, Set<String>> refreshRepositoryConfiguration(GHRepository ghRepository, Long userId)
            throws Exception {
        List<String> contributors = ghRepository.listContributors().toList().stream()
                .map(Contributor::getLogin)
                .toList();

        List<GVRepoUserConfiguration> existingConfigurations = getRepositoryConfiguration(ghRepository.getFullName(), userId);
        Set<String> existingUsernames = existingConfigurations.stream()
                .map(GVRepoUserConfiguration::getUsername)
                .collect(Collectors.toSet());

        GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Id(ghRepository.getFullName(), userId)
                .orElseThrow(() -> ResourceNotFoundException.of(GVRepo.class));

        List<GVRepoUserConfiguration> newConfigurations = contributors.stream()
                .filter(contributor -> !existingUsernames.contains(contributor))
                .map(contributor -> GVRepoUserConfiguration.of(gvRepo, contributor))
                .toList();

        List<GVRepoUserConfiguration> validConfigurations = existingConfigurations.stream()
                .filter(config -> contributors.contains(config.getUsername()))
                .toList();

        List<GVRepoUserConfiguration> deletedConfigurations = existingConfigurations.stream()
                .filter(config -> !contributors.contains(config.getUsername()))
                .toList();

        newConfigurations = gvRepoUserConfigurationRepository.saveAll(newConfigurations);

        gvRepoUserConfigurationRepository.deleteAll(deletedConfigurations);

        List<GVRepoUserConfiguration> updatedConfigurations = new ArrayList<>();
        updatedConfigurations.addAll(validConfigurations);
        updatedConfigurations.addAll(newConfigurations);

        return updatedConfigurations.stream()
        .collect(Collectors.toMap(GVRepoUserConfiguration::getUsername, GVRepoUserConfiguration::getAliases));
    }

    @Transactional
    public void updateGithubToken(String repositoryName, String login, String newGithubToken) throws Exception {
        GVRepo gvRepo = this.gvRepoRepository.findByNameAndUser_Username(repositoryName, login)
                .orElseThrow(() -> new ResourceNotFoundException("Not found repository"));
        GitHub github = GitHub.connect(login, newGithubToken);
        if (github.getMyself() == null) {
            throw new IllegalAccessException("Token invalido");
        }
        gvRepo.setToken(newGithubToken);
        this.gvRepoRepository.save(gvRepo);
    }

    @Transactional
    public void linkUserWithRepository(String login, String repositoryName, String token) {
        try {
            GVUser user = this.gvUserRepository.findByUsername(login).get();
            String tokenToUse = Objects.requireNonNullElse(token, user.getGithubToken());

            GitHub gitHub = GitHub.connect(login, tokenToUse);
            GHRepository ghRepository = gitHub.getRepository(repositoryName);
            GVRepo gvRepo = new GVRepo();
            gvRepo.setName(repositoryName);
            gvRepo.setRepositoryId(ghRepository.getId());
            gvRepo.setToken(tokenToUse);
            gvRepo.setUser(user);
            GVRepo savedGvRepo = gvRepoRepository.save(gvRepo);
            List<Contributor> ghContributors = ghRepository.listContributors().toList();
            List<GVRepoUserConfiguration> ghConfigurations = new ArrayList<>(ghContributors.size());
            ghContributors
                    .forEach(contributor -> ghConfigurations.add(GVRepoUserConfiguration.of(savedGvRepo, contributor)));
        } catch (Exception e) {
            throw LinkedException.linkGithub();
        }

    }

    @Transactional
    public void linkRepositoryWithWorkspace(String repositoryName, String workspaceName, Long userId) throws Exception {
        if (!gvUserRepository.existsById(userId))
            throw new ResourceNotFoundException("User", "ID", userId);

        GVRepo gvRepo = gvRepoRepository.findByNameAndUser_Id(repositoryName, userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserRepo not found"));
        ;

        if (gvRepo.hasLinkedWorkspace()) {
            throw new IllegalAccessError("No se puede enlazar más de un workspace a un repositorio");
        }

        GVWorkspace gvWorkspace = gvWorkspaceRepository.findByNameAndUser_Id(workspaceName, userId)
                .orElseThrow(() -> new ResourceNotFoundException("UserWorkspace not found"));
        ;

        if (Objects.equals(gvRepo.getWorkspace(), gvWorkspace)) {
            throw new Exception("Relation exist");
        }
        gvRepo.setWorkspace(gvWorkspace);
        gvRepoRepository.save(gvRepo);
    }

}
