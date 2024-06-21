package org.springframework.samples.gitvision.auth.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private Integer id;
	private String username;

	public JwtResponse(String accessToken, Integer id, String username) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
	}

	@Override
	public String toString() {
		return "JwtResponse [token=" + token + ", type=" + type + ", id=" + id + ", username=" + username + "]";
	}

}
