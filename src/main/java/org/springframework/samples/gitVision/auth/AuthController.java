package org.springframework.samples.gitvision.auth;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.auth.payload.request.LoginRequest;
import org.springframework.samples.gitvision.auth.payload.request.SignupRequest;
import org.springframework.samples.gitvision.auth.payload.response.JwtResponse;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.configuration.jwt.JwtUtils;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final JwtUtils jwtUtils;
	private final AuthService authService;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils,
			AuthService authService) {
		this.userService = userService;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
		this.authService = authService;
	}

	// @PostMapping("/signin")
	// public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
	// 	try{
	// 		User user = userService.findUserByUsername(loginRequest.getUsername());
	// 		if(!user.getGithubToken().equals(loginRequest.getGithubToken()))
	// 			return ResponseEntity.badRequest().body(MessageResponse.of("Incorrect Token"));
	// 		return ResponseEntity.ok().body(user);
	// 	}catch(ResourceNotFoundException exception){
	// 		return ResponseEntity.badRequest().body(MessageResponse.of("Bad Credentials"));
	// 	}
	// }

	@PostMapping("/signin")
	public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			String username = loginRequest.getUsername();
			String githubToken = loginRequest.getGithubToken();
			if(!userService.existsUserByUsername(username)){
				throw new ResourceNotFoundException("User", "username", username);
			}
			
			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, githubToken);
			
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			User user = userService.findUserById(userDetails.getId());
			return ResponseEntity.ok().body(new JwtResponse(jwt, user));
		} catch(Exception exception) {
			return ResponseEntity.badRequest().body(MessageResponse.of("Bad Credentials!"));
		} 
	}

	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
		Boolean isValid = jwtUtils.validateJwtToken(token);
		return ResponseEntity.ok(isValid);
	}

	
	@PostMapping("/signup")	
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		try {
            String username = signUpRequest.getUsername();
			String token = signUpRequest.getGithubToken();
            GitHub gitHub = GitHub.connect(username, token);
            GHUser user = gitHub.getMyself();	
			if(user == null || !user.getLogin().equals(signUpRequest.getUsername()))
				return ResponseEntity.badRequest().body(MessageResponse.of("Error: Username or token is incorrect"));
			if (userService.existsUserByUsername(username)) 
				return ResponseEntity.badRequest().body(MessageResponse.of("Error: Username is already taken!"));


			authService.createUser(user, token);
			return ResponseEntity.ok(MessageResponse.of("User registered successfully!"));

		} catch (IOException e) {
			return ResponseEntity.badRequest().body(MessageResponse.of("Error: Connection to github failed"));
		}
		
	}

}
