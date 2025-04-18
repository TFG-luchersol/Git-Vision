package org.springframework.samples.gitvision.relations.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.relations.repository.model.GVRepoUserConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface GVRepoUserConfigRepository extends RepositoryIdLong<GVRepoUserConfig> {

    List<GVRepoUserConfig> findByGvRepo(GVRepo gvRepo);

    Optional<GVRepoUserConfig> findByGvRepoAndUsername(GVRepo gvRepo, String username);

    List<GVRepoUserConfig> findByGvRepo_NameAndGvRepo_User_Id(String repositoryName, Long userId);

    Optional<GVRepoUserConfig> findByGvRepo_NameAndGvRepo_User_IdAndUsername(String gvRepo_Name, Long gvRepo_User_Id, String username);

}
