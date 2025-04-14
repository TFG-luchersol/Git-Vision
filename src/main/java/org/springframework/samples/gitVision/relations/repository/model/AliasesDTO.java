package org.springframework.samples.gitvision.relations.repository.model;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record AliasesDTO(@NotBlank String username,Set<String> aliases){
}
