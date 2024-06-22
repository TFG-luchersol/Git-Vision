package org.springframework.samples.gitvision.auth;

import java.io.IOException;

import jakarta.validation.Valid;

import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.auth.payload.request.LoginRequest;
import org.springframework.samples.gitvision.auth.payload.request.SignupRequest;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.configuration.jwt.JwtUtils;
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


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "The Authentication API based on JWT")
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
		try{
			// Authentication authentication = authenticationManager.authenticate(
			// 	new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getGithubToken()));

			// SecurityContextHolder.getContext().setAuthentication(authentication);
			// String jwt = jwtUtils.generateJwtToken(authentication);

			// UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

			// return ResponseEntity.ok().body(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername()));
			if(!userService.existsUserByUsername(loginRequest.getUsername()))
				return ResponseEntity.badRequest().body("Not exists user in local database");
			return ResponseEntity.ok().body(loginRequest.getUsername());
		}catch(BadCredentialsException exception){
			return ResponseEntity.badRequest().body("Bad Credentials!");
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
			String token = signUpRequest.getToken();
            GitHub gitHub = GitHub.connect(username, token);
            GHUser user = gitHub.getMyself();	
			
			if (userService.existsUserByUsername(username)) 
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));


			authService.createUser(user, token);
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

		} catch (IOException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: The username or token can be incorrect"));
		}
		
	}

}
