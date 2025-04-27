package org.springframework.samples.gitvision.relations.workspace;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.relations.workspace.model.AliasWorkspaceDTO;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspaceUserConfig;
import org.springframework.samples.gitvision.user.GVUserRepository;
import org.springframework.samples.gitvision.user.model.GVUser;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.samples.gitvision.workspace.model.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class GVWorkspaceService {

    private final GVWorkspaceRepository gvWorkspaceRepository;
    private final GVUserRepository gvUserRepository;
    private final GVWorkspaceUserConfigRepository gvWorkspaceUserConfigRepository;

    public GVWorkspaceService(GVWorkspaceRepository gvWorkspaceRepository, GVUserRepository gvUserRepository,
            GVWorkspaceUserConfigRepository gvWorkspaceUserConfigRepository) {
        this.gvWorkspaceRepository = gvWorkspaceRepository;
        this.gvUserRepository = gvUserRepository;
        this.gvWorkspaceUserConfigRepository = gvWorkspaceUserConfigRepository;
    }

    public List<GVWorkspaceUserConfig> getWorkspaceConfiguration(String workspaceName, Long userId) {
        GVWorkspace gvWorkspace = this.gvWorkspaceRepository.findByNameAndUser_Id(workspaceName, userId)
            .orElseThrow(() -> ResourceNotFoundException.of(GVWorkspace.class));
        return this.gvWorkspaceUserConfigRepository.findByGvWorkspace(gvWorkspace);
    }

    public List<GVWorkspace> getAllWorkspaceByUserId(Long userId) {
        List<GVWorkspace> gvWorkspaces = this.gvWorkspaceRepository.findAllByUser_Id(userId);
        return gvWorkspaces;
    }

    public List<GVWorkspace> getAllWorkspaceByUserIdNotLinked(Long userId) {
        List<GVWorkspace> gvWorkspaces = this.gvWorkspaceRepository.findAllByUser_IdNotLinked(userId);
        return gvWorkspaces;
    }

    public Map<String, Long> getTimeByUser(String workspaceId, Long userId) {
        String token = this.gvUserRepository.findById(userId).orElseThrow().getClockifyToken();
        return ClockifyApi.getTimeByUser(workspaceId, token);
    }

    public Map<String, Long> getTimeByUserInTask(String workspaceId, String taskName, Long userId) {
        String token = this.gvUserRepository.findById(userId).orElseThrow().getClockifyToken();
        return ClockifyApi.getTimeByUserByTaskName(workspaceId, taskName, token);
    }

    public void linkUserWithWorkspace(String workspaceId, String name, Long userId) {
        GVUser user = gvUserRepository.findById(userId).get();
        String token = user.getClockifyToken();
        ClockifyApi.getWorkspace(workspaceId, token);
        List<UserProfile> userProfiles = ClockifyApi.getUsers(workspaceId, token);
        GVWorkspace gvWorkspace = new GVWorkspace();
        gvWorkspace.setName(name);
        gvWorkspace.setUser(user);
        gvWorkspace.setWorkspaceId(workspaceId);
        gvWorkspaceRepository.save(gvWorkspace);

        var configs = userProfiles.stream()
                                  .map(profile -> GVWorkspaceUserConfig.of(gvWorkspace, profile))
                                  .toList();
        gvWorkspaceUserConfigRepository.saveAll(configs);
    }

    public GVWorkspaceUserConfig updateAliaUserConfigurations(String workspaceName, Long userId, AliasWorkspaceDTO aliasesDTO) {
        this.gvWorkspaceRepository.findByNameAndUser_Id(workspaceName, userId)
            .orElseThrow(() -> ResourceNotFoundException.of(GVWorkspace.class));

        var gvWorkspaceUserConfiguration = this.gvWorkspaceUserConfigRepository.findById(aliasesDTO.getId())
            .orElseThrow(() -> ResourceNotFoundException.of("Workspace User Configuration", "ID", aliasesDTO.getId()));

        gvWorkspaceUserConfiguration.setGithubUser(aliasesDTO.getGithubUser());

        return this.gvWorkspaceUserConfigRepository.save(gvWorkspaceUserConfiguration);
    }

    public List<GVWorkspaceUserConfig> refreshWorkspaceConfiguration(GVRepo gvRepo)
            throws Exception {
        String clockifyToken = gvRepo.getUser().getClockifyToken();
        String workspaceId = gvRepo.getWorkspace().getWorkspaceId();

        Set<UserProfile> userProfiles = ClockifyApi.getUsers(workspaceId, clockifyToken).stream()
                .collect(Collectors.toSet());

        List<GVWorkspaceUserConfig> userConfigurations = this.gvWorkspaceUserConfigRepository
                .findByGvWorkspace(gvRepo.getWorkspace());

        Map<String, UserProfile> userProfilesMap = userProfiles.stream()
                .collect(Collectors.toMap(UserProfile::getId, Function.identity()));

        Map<String, GVWorkspaceUserConfig> existingConfigurationsByUserId = userConfigurations.stream()
                .collect(Collectors.toMap(cfg -> cfg.getUserProfile().getId(), Function.identity()));

        Map<String, UserProfile> existingUserProfilesMap = existingConfigurationsByUserId.values().stream()
                .map(GVWorkspaceUserConfig::getUserProfile)
                .collect(Collectors.toMap(UserProfile::getId, Function.identity()));

        List<UserProfile> newUserConfigurations = userProfilesMap.entrySet().stream()
                .filter(entry -> !existingUserProfilesMap.containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        List<GVWorkspaceUserConfig> deletedUserConfigurations = existingConfigurationsByUserId.entrySet()
                .stream()
                .filter(entry -> !userProfilesMap.containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        List<GVWorkspaceUserConfig> updatedUserConfigurations = userProfilesMap.entrySet().stream()
                .filter(entry -> {
                    UserProfile nuevo = entry.getValue();
                    UserProfile existente = existingUserProfilesMap.get(entry.getKey());
                    return existente != null &&
                            (!Objects.equals(existente.getEmail(), nuevo.getEmail()) ||
                                    !Objects.equals(existente.getName(), nuevo.getName()));
                })
                .map(entry -> {
                    GVWorkspaceUserConfig cfg = existingConfigurationsByUserId.get(entry.getKey());
                    UserProfile nuevo = entry.getValue();
                    // Actualiza los atributos del perfil existente
                    UserProfile existente = cfg.getUserProfile();
                    existente.setEmail(nuevo.getEmail());
                    existente.setName(nuevo.getName());
                    return cfg;
                })
                .collect(Collectors.toList());

        List<GVWorkspaceUserConfig> nuevasConfiguraciones = newUserConfigurations.stream()
                .map(user -> {
                    GVWorkspaceUserConfig nueva = new GVWorkspaceUserConfig();
                    nueva.setGvWorkspace(gvRepo.getWorkspace());
                    nueva.setUserProfile(user);
                    return nueva;
                })
                .collect(Collectors.toList());

        gvWorkspaceUserConfigRepository.saveAll(nuevasConfiguraciones);
        gvWorkspaceUserConfigRepository.saveAll(updatedUserConfigurations);
        gvWorkspaceUserConfigRepository.deleteAll(deletedUserConfigurations);

        List<GVWorkspaceUserConfig> updatedConfigurations = this.gvWorkspaceUserConfigRepository
                .findByGvWorkspace(gvRepo.getWorkspace());

        return updatedConfigurations;

    }

}
