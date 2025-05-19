package org.springframework.samples.gitvision.relations.workspace;

import java.util.List;
import java.util.Optional;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspace;
import org.springframework.samples.gitvision.relations.workspace.model.GVWorkspaceUserConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface GVWorkspaceUserConfigRepository extends RepositoryIdLong<GVWorkspaceUserConfig>{

    List<GVWorkspaceUserConfig> findByGvWorkspace(GVWorkspace gvWorkspace);

    Optional<GVWorkspaceUserConfig> findByGvWorkspaceAndUserProfile_Name(GVWorkspace gvWorkspace, String name);

}
