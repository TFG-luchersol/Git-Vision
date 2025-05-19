package org.springframework.samples.gitvision.auth;

import java.util.Objects;

import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.auth.payload.request.LoginRequest;
import org.springframework.samples.gitvision.auth.payload.request.SignupRequest;
import org.springframework.samples.gitvision.auth.payload.response.JwtResponse;
import org.springframework.samples.gitvision.configuration.jwt.JwtUtils;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.user.GVUserService;
import org.springframework.samples.gitvision.user.model.GVUser;
import org.springframework.samples.gitvision.util.Checker;
import org.springframework.samples.gitvision.util.MessageResolver;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final GVUserService userService;
    private final JwtUtils jwtUtils;
    private final AuthService authService;
    private final MessageResolver msg;

    public AuthController(AuthenticationManager authenticationManager, GVUserService userService,
            JwtUtils jwtUtils, AuthService authService, MessageResolver msg) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.msg = msg;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            Checker.checkOrThrow(userService.existsUserByUsername(username),
                                ResourceNotFoundException.of("User", "username", username));

            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            GVUser user = this.userService.findUserById(userDetails.getId());
            JwtResponse jwtResponse = new JwtResponse(jwt, user);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            throw new BadCredentialsException(msg.get("auth.auth_controller.authenticate_user.bad_credentials"));
        }


    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        Boolean isValid = jwtUtils.validateJwtToken(token);
        return ResponseEntity.ok(isValid);
    }


    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult result) throws Exception {
        Checker.checkBindingResult(result);
        String username = signUpRequest.getUsername();
        String token = signUpRequest.getGithubToken();
        GitHub gitHub = GitHub.connect(username, token);
        signUpRequest.setGithubToken(token);

        GHUser user = gitHub.getMyself();

        Checker.checkOrBadRequest(user != null && Objects.equals(user.getLogin(), username),
                            "Error: Username or token is incorrect");
        Checker.checkOrBadRequest(!userService.existsUserByUsername(username),
                            "Error: Username is already taken!");

        authService.createUser(user, signUpRequest);
        return ResponseEntity.ok("User registered successfully!");

	}

}
