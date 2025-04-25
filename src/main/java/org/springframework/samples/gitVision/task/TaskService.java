package org.springframework.samples.gitvision.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.samples.gitvision.contributions.model.Contribution;
import org.springframework.samples.gitvision.contributions.model.ContributionByTime;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.repository.GVRepoUserConfigRepository;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.relations.repository.model.GVRepoUserConfig;
import org.springframework.samples.gitvision.relations.workspace.GVWorkspaceUserConfigRepository;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspaceUserConfig;
import org.springframework.samples.gitvision.task.model.Task;
import org.springframework.samples.gitvision.user.GVUserRepository;
import org.springframework.samples.gitvision.user.model.GVUser;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.samples.gitvision.util.GithubApi;
import org.springframework.samples.gitvision.util.GithubGraphQLApi;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final GVUserRepository gvUserRepository;
    private final GVRepoUserConfigRepository gvRepoUserConfigRepository;
    private final GVWorkspaceUserConfigRepository gvWorkspaceUserConfigRepository;

    public TaskService(GVUserRepository gvUserRepository, GVRepoUserConfigRepository gvRepoUserConfigRepository,
            GVWorkspaceUserConfigRepository gvWorkspaceUserConfigRepository){
        this.gvUserRepository = gvUserRepository;
        this.gvRepoUserConfigRepository = gvRepoUserConfigRepository;
        this.gvWorkspaceUserConfigRepository = gvWorkspaceUserConfigRepository;
    }

    public Long timeByName(String workspaceId, String projectId, String name, Long userId){
        GVUser user = gvUserRepository.findById(userId).orElseThrow();
        String clockifyToken = user.getClockifyToken();
        List<Task> tasks = ClockifyApi.getTasks(workspaceId, projectId, clockifyToken);
        Long totalDuration = tasks.stream()
                                    .filter(t -> t.getName().equals(name))
                                    .mapToLong(task -> task.getDuration().toMillis()).sum();
        return totalDuration;
    }

    public Map<String, ContributionByTime> getContributionByTime(GVRepo gvRepo, String issueName, String taskName, Long userId) throws Exception{
        Issue issue = GithubApi.connect(gvRepo)
                               .getIssueByExactTitle(issueName);
        Long issueNumber = issue.getNumber().longValue();
        return getContributionByTime(gvRepo, issueNumber, taskName, userId);
    }

    public Map<String, ContributionByTime> getContributionByTime(GVRepo gvRepo, Long issueNumber, String taskName, Long userId) throws Exception{
        GVUser user = gvUserRepository.findById(userId).orElseThrow();
        String githubToken = gvRepo.getToken(), clockifyToken = user.getClockifyToken();
        GVWorkspace gvWorkspace = gvRepo.getWorkspace();

        var configsRepository = gvRepoUserConfigRepository.findByGvRepo(gvRepo);
        List<Contribution> contributions = GithubGraphQLApi.connect(githubToken)
                        .getContributionsByIssueNumber(gvRepo.getName(), issueNumber);

        Map<String, ContributionByTime> res = new HashMap<>();

        for (GVRepoUserConfig gvRepoUserConfig : configsRepository) {
            Set<String> options = gvRepoUserConfig.getAllOptions();
            ContributionByTime contributionByTime = ContributionByTime.createNew();
            res.put(gvRepoUserConfig.getUsername(), contributionByTime);
            for (Contribution contribution : contributions) {
                if(options.contains(contribution.getAuthorName())) {
                    res.get(gvRepoUserConfig.getUsername()).mergeChanges(contribution);
                }
            }
        }

        if(gvWorkspace != null && user.getClockifyToken() != null) {
            String workspaceId = gvWorkspace.getWorkspaceId();
            Map<String, Long> associationUserTime = ClockifyApi.getTimeByUserByTaskName(workspaceId, taskName, clockifyToken);
            var configsWorkspace = this.gvWorkspaceUserConfigRepository.findByGvWorkspace(gvWorkspace);
            for (GVWorkspaceUserConfig gvWorkspaceUserConfig : configsWorkspace) {
                String userClockifyId = gvWorkspaceUserConfig.getUserProfile().getId();
                String githubUserName = gvWorkspaceUserConfig.getGithubUser();
                if(githubUserName == null || !res.containsKey(githubUserName)) continue;
                Long time = associationUserTime.getOrDefault(userClockifyId, 0L);
                res.get(githubUserName).mergeTime(time);
            }
        }

        return res;
    }


}
