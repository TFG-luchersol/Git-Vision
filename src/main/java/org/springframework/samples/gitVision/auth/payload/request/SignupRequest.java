package org.springframework.samples.gitvision.auth.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	
	@NotBlank
	private String username;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z]{4,}$")
	private String password;
	
	@NotBlank
	private String githubToken;

}
