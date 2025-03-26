package org.springframework.samples.gitvision.relations.repository.model;

import java.util.Arrays;
import java.util.List;

import org.kohsuke.github.GHRepository.Contributor;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "gv_repo_configurations")
public class GVRepoUserConfiguration {

    @ManyToOne
    private GVRepo gvRepo;

    Long userId;

    @ElementCollection
    @CollectionTable(name = "gv_repo_user_aliases", joinColumns = @JoinColumn(name = "user_id"))
    private List<String> alias;

    public static GVRepoUserConfiguration of(GVRepo gvRepo, Contributor contributor){
        GVRepoUserConfiguration gvRepoUserConfiguration = new GVRepoUserConfiguration();
        gvRepoUserConfiguration.setGvRepo(gvRepo);
        gvRepoUserConfiguration.setUserId(contributor.getId());
        gvRepoUserConfiguration.setAlias(Arrays.asList(contributor.getLogin()));
        return gvRepoUserConfiguration;
    }

}
