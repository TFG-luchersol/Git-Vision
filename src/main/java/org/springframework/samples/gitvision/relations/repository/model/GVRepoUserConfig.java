package org.springframework.samples.gitvision.relations.repository.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.kohsuke.github.GHRepository.Contributor;
import org.springframework.samples.gitvision.model.entity.EntityIdSequential;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "gv_repo_configurations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"gv_repo_id", "username"})
})
public class GVRepoUserConfig extends EntityIdSequential {

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GVRepo gvRepo;

    private String username;

    @ElementCollection
    @OnDelete(action = OnDeleteAction.CASCADE)
    @CollectionTable(name = "gv_repo_user_aliases", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "alias")
    private Set<String> aliases;

    public static GVRepoUserConfig of(GVRepo gvRepo, Contributor contributor){
        return of(gvRepo, contributor.getLogin());
    }

    public static GVRepoUserConfig of(GVRepo gvRepo, String contributor){
        GVRepoUserConfig gvRepoUserConfiguration = new GVRepoUserConfig();
        gvRepoUserConfiguration.setGvRepo(gvRepo);
        gvRepoUserConfiguration.setUsername(contributor);
        gvRepoUserConfiguration.setAliases(new LinkedHashSet<>());
        return gvRepoUserConfiguration;
    }

    @JsonIgnore
    public Set<String> getAllOptions() {
        Set<String> options = new HashSet<>(aliases.size() + 1);
        options.addAll(aliases);
        options.add(username);
        return options;
    }

    public Map<String, String> toOriginal() {
        Map<String, String> res = new HashMap<>();
        for (String alias : aliases) {
            res.put(alias, username);
        }
        res.put(username, username);
        return res;
    }

}
