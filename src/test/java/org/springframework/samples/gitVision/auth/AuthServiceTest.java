package org.springframework.samples.gitvision.auth;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.gitvision.auth.payload.request.LoginRequest;
import org.springframework.samples.gitvision.user.User;
import org.springframework.samples.gitvision.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
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
        // Crear y guardar un usuario de prueba
        User user = new User();
        user.setId(-1L);
        user.setUsername("testuser");
        user.setGithubToken(passwordEncoder.encode("testpassword"));
        userRepository.save(user);

        // Intentar autenticación
        try {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername("testuser");
            loginRequest.setGithubToken("testpassword");
            authController.authenticateUser(loginRequest);
            assertTrue(true); // Autenticación exitosa
        } catch (Exception e) {
            fail("Authentication failed");
        }
    }
}
