package org.springframework.samples.gitvision.auth.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank(message = "{auth.payload.request.login_request.username.not_blank}")
    private String username;

    @NotBlank(message = "{auth.payload.request.login_request.email.not_blank}")
    @Email(message = "{auth.payload.request.login_request.email.email}")
    private String email;

    @NotBlank(message = "{auth.payload.request.login_request.password.not_blank}")
    @Pattern(regexp = "^[a-zA-Z]{4,}$", message = "{auth.payload.request.login_request.password.pattern}")
    private String password;

    @NotBlank(message = "{auth.payload.request.login_request.githubToken.not_blank}")
    private String githubToken;

}
