package org.springframework.samples.gitVision.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>{

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Optional<User> findById(String id);
	
}
