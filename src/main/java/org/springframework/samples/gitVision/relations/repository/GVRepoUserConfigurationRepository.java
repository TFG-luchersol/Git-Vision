package org.springframework.samples.gitvision.relations.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.repository.model.GVRepo;
import org.springframework.samples.gitvision.relations.repository.model.GVRepoUserConfiguration;
import org.springframework.stereotype.Repository;

@Repository
public interface GVRepoUserConfigurationRepository extends RepositoryIdLong<GVRepoUserConfiguration> {

    List<GVRepoUserConfiguration> findByGvRepo(GVRepo gvRepo);

    List<GVRepoUserConfiguration> findByGvRepo_NameAndGvRepo_User_Id(String repositoryName, Long userId);

    Optional<GVRepoUserConfiguration> findByGvRepo_NameAndGvRepo_User_IdAndUsername(String gvRepo_Name, Long gvRepo_User_Id, String username);

}
