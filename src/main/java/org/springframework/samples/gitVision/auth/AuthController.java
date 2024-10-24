package org.springframework.samples.gitvision.auth;

import java.io.IOException;

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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;


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

	@PostMapping("/signin")
	public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			String username = loginRequest.getUsername();
			String password = loginRequest.getPassword();
			if(!userService.existsUserByUsername(username)){
				 new ResourceNotFoundException("User", "username", username);
			}
			
			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);
			
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			User user = userService.findUserById(userDetails.getId());
			
			return ResponseEntity.ok().body(new JwtResponse(jwt, user));
		} catch(ResourceNotFoundException exception) {
			return ResponseEntity.badRequest().body(MessageResponse.of(exception.getMessage()));
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
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult result) {
		try {
			if (result.hasErrors()) {
				FieldError firstError = result.getFieldErrors().get(0);
				String field = firstError.getField().substring(0, 1).toUpperCase() + 
							   firstError.getField().substring(1).toLowerCase(),
					   defaultMessage = firstError.getDefaultMessage(),
				       message = field + " " + defaultMessage;
				return ResponseEntity.badRequest().body(MessageResponse.of(message));
			}
			String username = signUpRequest.getUsername();
			String token = signUpRequest.getGithubToken();
            GitHub gitHub = GitHub.connect(username, token);
            GHUser user = gitHub.getMyself();
			if(user == null || !user.getLogin().equals(username))
				return ResponseEntity.badRequest().body(MessageResponse.of("Error: Username or token is incorrect"));
			if (userService.existsUserByUsername(username)) 
				return ResponseEntity.badRequest().body(MessageResponse.of("Error: Username is already taken!"));


			authService.createUser(user, signUpRequest);
			return ResponseEntity.ok(MessageResponse.of("User registered successfully!"));

		} catch (IOException e) {
			return ResponseEntity.badRequest().body(MessageResponse.of("Error: Connection to github failed"));
		}
		
	}

}
