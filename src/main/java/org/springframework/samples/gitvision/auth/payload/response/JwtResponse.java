package org.springframework.samples.gitvision.auth.payload.response;

import org.springframework.samples.gitvision.user.model.GVUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private GVUser user;

	public JwtResponse(String accessToken, GVUser user) {
		this.token = accessToken;
		this.user = user;
	}

}
