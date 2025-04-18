package org.springframework.samples.gitvision.task;

import java.util.List;
import java.util.Map;

import org.springframework.samples.gitvision.contributions.model.Contribution;
import org.springframework.samples.gitvision.issue.model.Issue;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
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

    public TaskService(GVUserRepository gvUserRepository){
        this.gvUserRepository = gvUserRepository;
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

    public Map<String, Long> timeByNameGroupByUserId(GVRepo gvRepo, String name, Long userId){
        GVUser user = gvUserRepository.findById(userId).orElseThrow();
        String githubToken = user.getGithubToken(), clockifyToken = user.getClockifyToken();

        String workspaceId = gvRepo.getWorkspace().getWorkspaceId();

        Long issueNumber = id;
        if(issueNumber == null) {
            Issue issue = GithubApi.getIssueByExactTitle(gvRepo.getName(), name, githubToken);
            issueNumber = issue.getNumber().longValue();
        }
        List<Contribution> contributions = GithubGraphQLApi.connect(gvRepo.getUser().getGithubToken())
                        .getContributionsByIssueNumber(gvRepo.getName(), userId);
        Map<String, Long> associationUserTime = ClockifyApi.getTimeByUserByTaskName(workspaceId, name, clockifyToken);
        return associationUserTime;
    }

}
