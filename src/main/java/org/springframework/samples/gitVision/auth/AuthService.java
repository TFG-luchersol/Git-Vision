package org.springframework.samples.gitvision.auth;

import java.io.IOException;

import org.kohsuke.github.GHUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitvision.auth.payload.request.SignupRequest;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public void createUser(GHUser request, SignupRequest signupRequest) {
		User user = new User();
		user.setId(request.getId());
		user.setUsername(request.getLogin());
		user.setEmail(signupRequest.getEmail());
		user.setAvatarUrl(request.getAvatarUrl());
		user.setGithubToken(signupRequest.getGithubToken());
		user.setPassword(encoder.encode(signupRequest.getPassword()));
		userService.saveUser(user);
	}

}
