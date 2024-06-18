package org.springframework.samples.gitVision.auth;

import jakarta.transaction.Transactional;

import java.io.IOException;

import org.kohsuke.github.GHUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitVision.user.User;
import org.springframework.samples.gitVision.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserService userService;

	@Autowired
	public AuthService(UserService userService) {
		this.userService = userService;
	}

	@Transactional
	public void createUser(GHUser request, String username, String token) {
		User user = new User();
		try {
			user.setId(request.getNodeId());
			user.setUsername(username);
			user.setGithubToken(token);
			user.setEmail(request.getEmail());
			user.setAvatarUrl(request.getAvatarUrl());
		} catch (IOException e) {
			System.out.println("Error: Error: User instantiation failed");
		}
		
		userService.saveUser(user);
	}

}
