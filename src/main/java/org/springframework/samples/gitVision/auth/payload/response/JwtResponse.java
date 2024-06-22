package org.springframework.samples.gitvision.auth.payload.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {

	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String password;

	public JwtResponse(String accessToken, Long id, String username, String password) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return "JwtResponse [token=" + token + ", type=" + type + ", id=" + id + ", username=" + username + 
		", password=" + password + "]";
	}

}
