package org.springframework.samples.gitvision.relations.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.githubUser.model.GithubUser;
import org.springframework.samples.gitvision.relations.repository.model.AliasesDTO;
import org.springframework.samples.gitvision.relations.repository.model.BranchInfo;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.relations.repository.model.GVRepoUserConfig;
import org.springframework.samples.gitvision.relations.repository.model.ReleaseInfo;
import org.springframework.samples.gitvision.util.MessageResolver;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/relation/repository")
@Tag(name = "Relation repository")
@SecurityRequirement(name = "bearerAuth")
public class GVRepoController {

    private final GVRepoService gvRepoService;
    private final MessageResolver msg;

    public GVRepoController(GVRepoService gvRepoService, MessageResolver msg) {
        this.gvRepoService = gvRepoService;
        this.msg = msg;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getAllRepositoriesByUserId(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        Map<String, List<String>>ownerRepositories = this.gvRepoService.getAllRepositories(userId);
        return ResponseEntity.ok(ownerRepositories);
    }

    @GetMapping("/workspace")
    public ResponseEntity<Map<String, GVRepo>> getAllRepositoriesRelations(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl)
        {
        Long userId = userDetailsImpl.getId();
        Map<String, GVRepo> relations = this.gvRepoService.getAllRepositoriesRelations(userId);
        return ResponseEntity.ok(relations);
    }

    @GetMapping("/not_linked")
    public ResponseEntity<Map<String, List<String>>> getAllRepositoriesByUserIdAndNotLinked(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        Map<String, List<String>>ownerRepositories = this.gvRepoService.getAllRepositoriesNotLinked(userId);
        return ResponseEntity.ok(ownerRepositories);
    }


    @GetMapping("/owners")
    public ResponseEntity<List<String>> getAllOwnersByUserId(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        Long userId = userDetailsImpl.getId();
        List<String> owners = this.gvRepoService.getAllOwnersByUserId(userId);
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/{owner}/{repo}")
    public ResponseEntity<GVRepo> getRepository(@PathVariable String owner,
                                           @PathVariable String repo,
                                           @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        Long userId = userDetailsImpl.getId();
        String respositoryName = owner + "/" + repo;
        GVRepo gvRepo = this.gvRepoService.getGvRepoByNameAndUser_Id(respositoryName, userId);
        return ResponseEntity.ok(gvRepo);
    }

    @GetMapping("/{owner}/{repo}/branches")
    public ResponseEntity<List<BranchInfo>> getRepositoryBranches(@PathVariable String owner,
                                           @PathVariable String repo,
                                           @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, userDetailsImpl.getUsername());
        String defaultBranch = ghRepository.getDefaultBranch();

        List<BranchInfo> branchInfos = ghRepository.getBranches().keySet().stream()
                .map(name -> new BranchInfo(name, name.equals(defaultBranch)))
                .toList();

        return ResponseEntity.ok(branchInfos);
    }

    @GetMapping("/{owner}/{repo}/releases")
    public ResponseEntity<List<ReleaseInfo>> getRepositoryReleases(@PathVariable String owner,
                                           @PathVariable String repo,
                                           @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, userDetailsImpl.getUsername());
        List<ReleaseInfo> res = ghRepository.listReleases().toList()
                    .stream()
                    .sorted(Comparator.comparing(GHRelease::getPublished_at).reversed())
                    .map(new Function<GHRelease, ReleaseInfo>() {
                        boolean first = true;
                        @Override
                        public ReleaseInfo apply(GHRelease release) {
                            ReleaseInfo info = new ReleaseInfo(release.getName(), first);
                            first = false;
                            return info;
                        }
                    }).toList();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{owner}/{repo}/contributors")
    public ResponseEntity<List<GithubUser>> getContributors(@PathVariable String owner,
                                           @PathVariable String repo,
                                           @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String login = userDetailsImpl.getUsername();
        GHRepository ghRepository = this.gvRepoService.getRepository(owner, repo, login);
        List<GithubUser> contributors = this.gvRepoService.getContributors(ghRepository);
        return ResponseEntity.ok(contributors);
    }

    @GetMapping("/{owner}/{repo}/user_alias")
    public ResponseEntity<Map<String, Set<String>>> getRepositoryConfiguration(
            @PathVariable String owner, @PathVariable String repo,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        String repositoryName = owner + "/" + repo;
        List<GVRepoUserConfig> gvRepoUserConfiguration = gvRepoService.getRepositoryConfiguration(repositoryName, userDetailsImpl.getId());
        Map<String, Set<String>> aliases = gvRepoUserConfiguration.stream()
            .collect(Collectors.toMap(GVRepoUserConfig::getUsername, GVRepoUserConfig::getAliases));
        return ResponseEntity.ok(aliases);
    }

    @PutMapping("/{owner}/{repo}/url_image/refresh")
    public ResponseEntity<String> refreshUrlImage(
            @PathVariable String owner,
            @PathVariable String repo,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        String repositoryName = owner + "/" + repo;
        String urlImage = gvRepoService.refreshUrlImage(userDetailsImpl.getUsername(), repositoryName);
        return ResponseEntity.ok(urlImage);
    }

    @PutMapping("/{owner}/{repo}/user_alias")
    public ResponseEntity<?> updateAliases(
            @PathVariable String owner, @PathVariable String repo,
            @RequestBody @Valid AliasesDTO aliasesDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        String repositoryName = owner + "/" + repo;
        gvRepoService.updateAliaUserConfigurations(repositoryName, userDetailsImpl.getId(), aliasesDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{owner}/{repo}/user_alias/refresh")
    public ResponseEntity<Map<String, Set<String>>> refreshRepositoryConfiguration(
            @PathVariable String owner, @PathVariable String repo,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        GHRepository ghRepository = gvRepoService.getRepository(owner, repo, userDetailsImpl.getUsername());
        Map<String, Set<String>> aliases = gvRepoService.refreshRepositoryConfiguration(ghRepository, userDetailsImpl.getId());
        return ResponseEntity.ok(aliases);
    }


    @PutMapping("/{owner}/{repo}/token")
    public ResponseEntity<String> updateGithubToken(@PathVariable String owner,
                                    @PathVariable String repo,
                                    @RequestBody String newGithubToken,
                                    @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String login = userDetailsImpl.getUsername();
        String repositoryName = owner + "/" + repo;
        this.gvRepoService.updateGithubToken(repositoryName, login, newGithubToken);
        return ResponseEntity.ok("Token cambiado");
    }

    @PostMapping("/{owner}/{repo}")
    public void linkUserWithRepository(@PathVariable String repo,
                                       @PathVariable String owner,
                                       @RequestParam(required = false) String token,
                                       @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        String login = userDetailsImpl.getUsername();
        String repositoryName = owner + "/" + repo;
        this.gvRepoService.linkUserWithRepository(login, repositoryName, token);
    }

    @PostMapping("/{owner}/{repo}/linker")
    public ResponseEntity<String> linkRepositoryWithWorkspace(
                                            @PathVariable String repo,
                                            @PathVariable String owner,
                                            @RequestParam String workspaceName,
                                            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        Long userId = userDetailsImpl.getId();
        String repositoryName = owner + "/" + repo;
        this.gvRepoService.linkRepositoryWithWorkspace(repositoryName, workspaceName, userId);
        String message = msg.get("api.v1.relation.repository.owner.repo.linker.post.response", repositoryName, workspaceName);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{owner}/{repo}")
    public ResponseEntity<String> deleteRepository(
            @PathVariable String repo,
            @PathVariable String owner,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        Long userId = userDetailsImpl.getId();
        String repositoryName = owner + "/" + repo;
        this.gvRepoService.deleteRepository(repositoryName, userId);
        String message = msg.get("api.v1.relation.repository.owner.repo.delete.response");
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{owner}/{repo}/linker")
    public ResponseEntity<String> deleteRelationWorkspace(
            @PathVariable String repo,
            @PathVariable String owner,
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        Long userId = userDetailsImpl.getId();
        String repositoryName = owner + "/" + repo;
        this.gvRepoService.deleteRelation(repositoryName, userId);
        String message = msg.get("api.v1.relation.repository.owner.repo.linker.delete.response");
        return ResponseEntity.ok(message);
    }

}


