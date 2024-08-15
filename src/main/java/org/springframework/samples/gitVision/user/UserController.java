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

import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.exceptions.AccessDeniedException;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.samples.gitvision.util.RestPreconditions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

// @SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<User>> findAll() {
		List<User> res = (List<User>) userService.findAll();
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable("id") Long id) {
		return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<User> create(@RequestBody @Valid User user) {
		User savedUser = userService.saveUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	@PutMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<User> update(@PathVariable("userId") Long id, @RequestBody @Valid User user) {
		RestPreconditions.checkNotNull(userService.findUserById(id), "User", "ID", id);
		return ResponseEntity.ok(this.userService.updateUser(user, id));
	}

	@PutMapping("/user/{username}/token/github")
	public ResponseEntity<MessageResponse> updateGithubToken(@PathVariable String username, @RequestBody String githubToken) {
		try {
			User user = userService.updateGithubToken(username, githubToken);
			Information customMap = Information.empty().put("githubToken", githubToken);
			return ResponseEntity.ok(MessageResponse.of("Github Token has been updated", customMap));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(MessageResponse.of("Failed!"));
		}
	}

	@PutMapping("/user/{username}/token/clockify")
	public ResponseEntity<MessageResponse> updateClockifyToken(@PathVariable String username, @RequestBody String clockifyToken) {
		try {
			User user = userService.updateClockifyToken(username, clockifyToken);
			Information customMap = Information.empty().put("clockifyToken", clockifyToken);
			return ResponseEntity.ok(MessageResponse.of("Github Token has been updated"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(MessageResponse.of("Failed!"));
		}
	}

	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<MessageResponse> delete(@PathVariable("userId") Long id) {
		RestPreconditions.checkNotNull(userService.findUserById(id), "User", "ID", id);
		if (!Objects.equals(userService.findCurrentUser().getId(), id)) {
			userService.deleteUserById(id);
			return ResponseEntity.ok(new MessageResponse("User deleted!"));
		} else
			throw new AccessDeniedException("You can't delete yourself!");
	}

}
