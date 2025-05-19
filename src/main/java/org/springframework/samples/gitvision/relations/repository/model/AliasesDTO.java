package org.springframework.samples.gitvision.relations.repository.model;

import java.util.Set;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AliasesDTO {

    @NotBlank(message = "{relations.repository.model.aliases_dto.username.not_blank}")
    private String username;

    private Set<String> aliases;

    public AliasesDTO(String username, Set<String> aliases) {
        this.username = username;
        this.aliases = aliases;
    }

    @AssertTrue(message = "{relations.repository.model.aliases_dto.aliases.assert_true}")
    public boolean isValid() {
        return aliases == null || aliases.stream().allMatch(alias -> !alias.isBlank());
    }

}
