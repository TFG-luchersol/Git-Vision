package org.springframework.samples.gitvision.relations.workspace.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AliasWorkspaceDTO(@NotNull Long id, @NotBlank String githubUser) {

}
