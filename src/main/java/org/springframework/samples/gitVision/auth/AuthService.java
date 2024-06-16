package org.springframework.samples.gitVision.auth;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.gitVision.auth.payload.request.SignupRequest;
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
	public void createUser(@Valid SignupRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		String strRoles = request.getAuthority();	
	}

}
