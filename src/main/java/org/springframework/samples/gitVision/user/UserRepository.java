package org.springframework.samples.gitvision.user;

import java.util.Optional;

import org.springframework.samples.gitvision.model.repository.BaseRepositoryIdLong;

public interface UserRepository extends BaseRepositoryIdLong<User>{

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Optional<User> findById(Long id);
	
}
