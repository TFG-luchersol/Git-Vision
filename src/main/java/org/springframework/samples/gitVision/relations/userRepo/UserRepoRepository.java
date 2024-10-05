package org.springframework.samples.gitvision.relations.userRepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepoRepository extends RepositoryIdLong<UserRepo> {
    
    @Query("select ur.name from UserRepo ur where ur.user.id = :userId")
    List<String> findAllRepository_NameByUser_Id(Long userId);

    boolean existsByNameAndUser_Id(String repositoryName, Long userId);

    Optional<UserRepo> findByNameAndUser_Id(String repositoryName, Long userId);

    Optional<UserRepo> findByNameAndUser_Username(String repositoryName, String username);

}
