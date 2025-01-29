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

import org.kohsuke.github.GitHub;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.gitvision.exceptions.ResourceNotFoundException;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository){
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	}

	@Transactional(readOnly = true)
	public User findUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
	}

	@Transactional(readOnly = true)
	public User findCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			throw new ResourceNotFoundException("Nobody authenticated!");
		else
			return userRepository.findByUsername(auth.getName())
					.orElseThrow(() -> new ResourceNotFoundException("User", "Username", auth.getName()));
	}

	@Transactional(readOnly = true)
	public Iterable<User> findAll() {
		return userRepository.findAll();
	}

	public Boolean existsUserByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Transactional
	public User updateUser(@Valid User user, Long idToUpdate) {
		User toUpdate = findUserById(idToUpdate);
		BeanUtils.copyProperties(user, toUpdate, "id");
		userRepository.save(toUpdate);
		return toUpdate;
	}

	@Transactional 
	public void updateGithubToken(String username, String githubToken) throws Exception{
		GitHub gitHub = GitHub.connect(username, githubToken);
	
		if(gitHub.getMyself() == null)
			throw new IllegalAccessError("Error: fail to connect Github with new token");
		User user = findUserByUsername(username);
		user.setGithubTokenAndEncrypt(githubToken);
		userRepository.save(user);
	}

	@Transactional 
	public void updateClockifyToken(String username, String clockifyToken) throws Exception{
		
		try {
			ClockifyApi.getCurrentUser(clockifyToken);
		} catch (Exception e) {
			throw new IllegalAccessError("Error en conexión a clockify con token");
		}
			
		User user = findUserByUsername(username);
		user.setClockifyTokenAndEncrypt(clockifyToken);
		userRepository.save(user);
	}


	@Transactional
	public User saveUser(User user) throws DataAccessException {
		userRepository.save(user);
		return user;
	}

	@Transactional
	public void deleteUserById(Long id) {
		User toDelete = findUserById(id);
		this.userRepository.delete(toDelete);
	}


}
