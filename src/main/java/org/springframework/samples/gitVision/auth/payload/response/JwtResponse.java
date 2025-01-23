package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.samples.gitvision.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private User user;

	public JwtResponse(String accessToken, User user) {
		this.token = accessToken;
		this.user = user;
	}

}
