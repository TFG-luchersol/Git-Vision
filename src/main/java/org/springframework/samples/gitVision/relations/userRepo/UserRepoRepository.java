package org.springframework.samples.gitvision.relations.userRepo;

import java.util.List;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.relations.userRepo.model.UserRepo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepoRepository extends RepositoryIdLong<UserRepo> {
    
    List<String> findAllRepository_NameByUser_Id(Long userId);

}
