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

import org.springframework.http.HttpStatus;
import org.springframework.samples.gitvision.auth.payload.response.MessageResponse;
import org.springframework.samples.gitvision.auth.payload.response.OkResponse;
import org.springframework.samples.gitvision.configuration.services.UserDetailsImpl;
import org.springframework.samples.gitvision.util.Information;
import org.springframework.samples.gitvision.util.RestPreconditions;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	public MessageResponse findById(@PathVariable Long id,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		Information information = Information.create("user", userService.findUserById(id));
		return OkResponse.of(information);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MessageResponse create(@RequestBody @Valid GVUser user) {
		GVUser savedUser = userService.saveUser(user);
		Information information = Information.create("user", savedUser);
		return OkResponse.of(information);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MessageResponse update(@PathVariable Long id, @RequestBody @Valid GVUser user) {
		RestPreconditions.checkNotNull(userService.findUserById(id), "User", "ID", id);
		GVUser updatedUser = this.userService.updateUser(user, id);
		Information information = Information.create("updatedUser", updatedUser);
		return OkResponse.of(information);
	}

	@PutMapping("/user/token/github")
	public MessageResponse updateGithubToken(@RequestBody String githubToken,
											 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String username = userDetailsImpl.getUsername();
        userService.updateGithubToken(username, githubToken);
        Information information = Information.create("message", "Github Token has been updated")
                                            .put("githubToken", githubToken);
        return OkResponse.of(information);
	}

	@PutMapping("/user/token/clockify")
	public MessageResponse updateClockifyToken(@RequestBody String clockifyToken,
											   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) throws Exception {
        String username = userDetailsImpl.getUsername();
        userService.updateClockifyToken(username, clockifyToken);
        Information information = Information.create("message", "Github Token has been updated").put("clockifyToken", clockifyToken);
        return OkResponse.of(information);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MessageResponse delete(@PathVariable Long id) {
		RestPreconditions.checkNotNull(userService.findUserById(id), "User", "ID", id);
		userService.deleteUserById(id);
		return OkResponse.of("User deleted!");
	}

}
