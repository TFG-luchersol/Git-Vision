package org.springframework.samples.gitvision.auth;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.gitvision.auth.AuthController;
import org.springframework.samples.gitvision.auth.payload.request.LoginRequest;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    public void testAuthentication() {
        User user = new User();
        user.setId(-1L);
        user.setUsername("testuser");
        user.setGithubToken(passwordEncoder.encode("testpassword"));
        userRepository.save(user);

        try {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername("testuser");
            loginRequest.setGithubToken("testpassword");
            authController.authenticateUser(loginRequest);
            assertTrue(true); 
        } catch (Exception e) {
            fail("Authentication failed");
        }
    }
}
