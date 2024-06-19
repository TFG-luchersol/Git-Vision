package org.springframework.samples.gitVision.user;

import java.util.Optional;

import org.springframework.samples.gitVision.model.BaseRepository;

public interface UserRepository extends BaseRepository<User>{

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Optional<User> findById(String id);
	
}
