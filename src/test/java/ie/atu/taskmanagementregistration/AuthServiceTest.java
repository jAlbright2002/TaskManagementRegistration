package ie.atu.taskmanagementregistration;

import ie.atu.taskmanagementregistration.Authentication.AuthService;
import ie.atu.taskmanagementregistration.Authentication.LoginUser;
import ie.atu.taskmanagementregistration.Config.JwtConfig;
import ie.atu.taskmanagementregistration.User.User;
import ie.atu.taskmanagementregistration.User.UserDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserDB userDB;

    @Mock
    private BCryptPasswordEncoder passEncoder;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private JwtConfig jwt;

    @BeforeEach
    void setUp() {

    }

    @Test
    void successfulUserRegistration() {
        User user = new User();
        user.setEmail("sean@atu.com");
        user.setFirstName("Sean");

        when(userDB.findByEmail("sean@atu.com")).thenReturn(Optional.empty());

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(Object.class));

        ResponseEntity<String> response = authService.register(user);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Thank you for joining us Sean", response.getBody());
    }

    @Test
    void failedUserRegistrationAlreadyExists() {
        User user = new User();
        user.setEmail("james@atu.com");
        user.setFirstName("James");

        User existingUser = new User();
        existingUser.setEmail("james@atu.com");

        when(userDB.findByEmail("james@atu.com")).thenReturn(Optional.of(existingUser));

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(Object.class));

        ResponseEntity<String> response = authService.register(user);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void successfulUserLogin() {
        LoginUser loginUser = new LoginUser();
        loginUser.setEmail("patrick@atu.com");
        loginUser.setPassword("correctPassword");

        User user = new User();
        user.setEmail("patrick@atu.com");
        user.setPassword("encodedPassword");
        user.setFirstName("Patrick");

        when(userDB.findByEmail("patrick@atu.com")).thenReturn(Optional.of(user));
        when(passEncoder.matches("correctPassword", "encodedPassword")).thenReturn(true);
        when(jwt.generateToken("patrick@atu.com")).thenReturn("jwtToken");

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(Object.class));

        ResponseEntity<String> response = authService.login(loginUser);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Welcome Patrick\nToken: jwtToken", response.getBody());
    }

    @Test
    void failedUserLoginWrongPass() {
        LoginUser loginUser = new LoginUser();
        loginUser.setEmail("mark@atu.com");
        loginUser.setPassword("wrongPass");

        User user = new User();
        user.setEmail("mark@atu.com");
        user.setPassword("encryptedPassword");

        when(userDB.findByEmail("mark@atu.com")).thenReturn(Optional.of(user));
        when(passEncoder.matches("wrongPass", "encryptedPassword")).thenReturn(false);

        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(Object.class));

        ResponseEntity<String> response = authService.login(loginUser);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Password incorrect", response.getBody());
    }

}
