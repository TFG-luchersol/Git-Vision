package org.springframework.samples.gitvision.relations.workspace.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AliasWorkspaceDTO {

    @NotNull(message = "{relations.repository.model.alias_workspace_dto.id.not_null}")
    private Long id;

    @NotBlank(message = "{relations.repository.model.alias_workspace_dto.githubUser.not_blank}")
    private String githubUser;

}
