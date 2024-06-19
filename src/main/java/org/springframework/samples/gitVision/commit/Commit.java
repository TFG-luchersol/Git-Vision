package org.springframework.samples.gitVision.commit;

import java.time.LocalDate;

import org.kohsuke.github.GHCommit;
import org.springframework.samples.gitVision.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Commit extends BaseEntity{
    
    String description;

    @NotNull
    LocalDate date;

}
