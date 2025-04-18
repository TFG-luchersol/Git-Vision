package org.springframework.samples.gitvision.relations.repository.model;

import java.util.Set;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

public record AliasesDTO(@NotBlank String username, Set<String> aliases){

    @AssertTrue(message = "No debe de existir alias en blanco")
    public boolean isValid() {
        return aliases.isEmpty() || aliases.stream().allMatch(i -> !i.isBlank());
    }

}
