package org.springframework.samples.gitvision.commit.model;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.samples.gitvision.collaborator.model.Collaborator;
import org.springframework.samples.gitvision.model.entity.BaseEntity;
import org.springframework.samples.gitvision.model.entity.EntityIdString;
import org.springframework.samples.gitvision.repository.Repository;
import org.springframework.samples.gitvision.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "commits")
public class Commit extends EntityIdString {
    
    String message;

    @NotNull
    LocalDateTime date;

    @PositiveOrZero
    int additions;

    @PositiveOrZero
    int deletions;
    
    @ManyToOne
    Collaborator author;

    @ManyToOne
    Repository repository;
    
    public CommitType getCommitType() {
        String regex = "^\\s*\\[\\s*(\\w+)\\s*\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(getMessage());
        if(matcher.find()) {
            String type = matcher.group(1).trim().toUpperCase();
            try {
                return CommitType.valueOf(type);
            } catch (Exception e) {
                return null;
            }
        } 
        return null;
    }

}
