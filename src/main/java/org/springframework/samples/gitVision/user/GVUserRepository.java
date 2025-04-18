package org.springframework.samples.gitvision.user;

import java.util.Optional;

import org.springframework.samples.gitvision.model.repository.RepositoryIdLong;
import org.springframework.samples.gitvision.user.model.GVUser;

public interface GVUserRepository extends RepositoryIdLong<GVUser>{

	Optional<GVUser> findByUsername(String username);

	String findGithubTokenByUsername(String username);

	boolean existsByUsername(String username);

}
