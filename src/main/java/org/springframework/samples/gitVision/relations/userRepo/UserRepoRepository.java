package org.springframework.samples.gitvision.relations.userRepo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepoRepository extends RepositoryIdLong<UserRepo> {
    
    @Query("select ur.name from UserRepo ur where ur.user.id = :userId")
    List<String> findAllRepository_NameByUser_Id(Long userId);

}
