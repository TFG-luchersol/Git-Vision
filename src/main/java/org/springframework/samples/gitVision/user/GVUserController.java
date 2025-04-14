/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.gitvision.user;

import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User")
public class GVUserController {

	private final GVUserService userService;

	public GVUserController(GVUserService userService) {
		this.userService = userService;
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

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		userService.deleteUserById(id);
		return ResponseEntity.ok("User deleted!");
	}

}
