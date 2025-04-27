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
import org.springframework.samples.gitvision.user.model.GVUser;
import org.springframework.samples.gitvision.util.Checker;
import org.springframework.samples.gitvision.util.ClockifyApi;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class GVUserService {

	private final GVUserRepository gvUserRepository;

	public GVUserService(GVUserRepository gvUserRepository){
		this.gvUserRepository = gvUserRepository;
	}

	@Transactional(readOnly = true)
	public GVUser findUserByUsername(String username) {
		return gvUserRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	}

	@Transactional(readOnly = true)
	public GVUser findUserById(Long id) {
		return gvUserRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
	}

	@Transactional(readOnly = true)
	public GVUser findCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null)
			throw new ResourceNotFoundException("Nobody authenticated!");
		else
			return gvUserRepository.findByUsername(auth.getName())
					.orElseThrow(() -> new ResourceNotFoundException("User", "Username", auth.getName()));
	}

	@Transactional(readOnly = true)
	public Iterable<GVUser> findAll() {
		return gvUserRepository.findAll();
	}

	public Boolean existsUserByUsername(String username) {
		return gvUserRepository.existsByUsername(username);
	}

	@Transactional
	public GVUser updateUser(@Valid GVUser user, Long idToUpdate) {
		GVUser toUpdate = findUserById(idToUpdate);
		BeanUtils.copyProperties(user, toUpdate, "id");
		gvUserRepository.save(toUpdate);
		return toUpdate;
	}

	@Transactional
	public void updateGithubToken(String username, String githubToken) throws Exception{
        Checker.checkOrThrow(gvUserRepository.existsByUsername(username),
                             ResourceNotFoundException.of("User", "username", username));
		try {
            GitHub.connect(username, githubToken).getMyself();
        } catch (Exception e) {
            throw new IllegalAccessException("Error: fail to connect Github with new token");
        }

		GVUser user = findUserByUsername(username);
		user.setGithubToken(githubToken);
		gvUserRepository.save(user);
	}

	@Transactional
	public void updateClockifyToken(String username, String clockifyToken) throws Exception{

		try {
			ClockifyApi.getCurrentUser(clockifyToken);
		} catch (Exception e) {
			throw new IllegalAccessError("Error en conexión a clockify con token");
		}

		GVUser user = findUserByUsername(username);
		user.setClockifyToken(clockifyToken);
		gvUserRepository.save(user);
	}

    @Transactional
	public void deleteClockifyToken(String username) throws Exception{
		GVUser user = findUserByUsername(username);
		user.setClockifyToken(null);
		gvUserRepository.save(user);
	}



	@Transactional
	public GVUser saveUser(GVUser user) throws DataAccessException {
		gvUserRepository.save(user);
		return user;
	}

	@Transactional
	public void deleteUserById(Long id) {
        Checker.checkOrThrow(gvUserRepository.existsById(id),
                             ResourceNotFoundException.of("User", "ID", id));
		GVUser toDelete = findUserById(id);
		this.gvUserRepository.delete(toDelete);
	}


}
