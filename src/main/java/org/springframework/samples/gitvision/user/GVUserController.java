package org.springframework.samples.gitvision.user;

import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.user.model.GVUser;
import org.springframework.samples.gitvision.util.MessageResolver;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users")
@SecurityRequirement(name = "bearerAuth")
public class GVUserController {

	private final GVUserService userService;
    private final MessageResolver msg;

	public GVUserController(GVUserService userService, MessageResolver msg) {
		this.userService = userService;
        this.msg = msg;
	}

	@GetMapping("/{id}")
	public ResponseEntity<GVUser> findById(@PathVariable Long id,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        GVUser gvUser = userService.findUserById(id);
        return ResponseEntity.ok(gvUser);
	}

	@PostMapping
	public ResponseEntity<GVUser> create(@RequestBody @Valid GVUser user) {
		GVUser savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
	}

	@PutMapping("/{id}")
	public ResponseEntity<GVUser> update(@PathVariable Long id, @RequestBody @Valid GVUser user) {
		GVUser updatedUser = this.userService.updateUser(user, id);
		return ResponseEntity.ok(updatedUser);
	}

	@PutMapping("/user/token/github")
	public ResponseEntity<String> updateGithubToken(@RequestBody String githubToken,
											 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String username = userDetailsImpl.getUsername();
        userService.updateGithubToken(username, githubToken);
        return ResponseEntity.ok(githubToken);
	}

	@PutMapping("/user/token/clockify")
	public ResponseEntity<String> updateClockifyToken(@RequestBody String clockifyToken,
											   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String username = userDetailsImpl.getUsername();
        userService.updateClockifyToken(username, clockifyToken);
        return ResponseEntity.ok(clockifyToken);
	}

    @DeleteMapping("/user/token/clockify")
	public ResponseEntity<String> deleteClockifyToken(
            @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String username = userDetailsImpl.getUsername();
        userService.deleteClockifyToken(username);
        String message = msg.get("api.v1.users.user.token.clockify.delete.response");
        return ResponseEntity.ok(message);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		userService.deleteUserById(id);
        String message = msg.get("api.v1.users.id.delete.response");
		return ResponseEntity.ok(message);
	}

}
