package org.springframework.samples.gitvision.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "{auth.payload.request.login_request.username.not_blank}")
    private String username;

    @NotBlank(message = "{auth.payload.request.login_request.password.not_blank}")
    private String password;
}
