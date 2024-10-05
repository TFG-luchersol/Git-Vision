package org.springframework.samples.gitvision.user;

import java.util.Optional;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;

public interface UserRepository extends RepositoryIdLong<User>{

	Optional<User> findByUsername(String username);

	String findGithubTokenByUsername(String username);

	boolean existsByUsername(String username);
	
}
