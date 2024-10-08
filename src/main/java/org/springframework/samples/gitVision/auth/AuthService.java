package org.springframework.samples.gitvision.auth;

import jakarta.transaction.Transactional;

import java.io.IOException;

import org.kohsuke.github.GHUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final PasswordEncoder encoder;
	private final UserService userService;

	@Autowired
	public AuthService(UserService userService, PasswordEncoder encoder) {
		this.userService = userService;
		this.encoder = encoder;
	}

	@Transactional
	public void createUser(GHUser request, String token) {
		User user = new User();
		try {
			user.setId(request.getId());
			user.setUsername(request.getLogin());
			user.setEmail(request.getEmail());
			user.setAvatarUrl(request.getAvatarUrl());
			user.setGithubToken(encoder.encode(token));

			userService.saveUser(user);
		} catch (IOException e) {
			System.out.println("Error: Failed getting email");
		}
		
	}

}
