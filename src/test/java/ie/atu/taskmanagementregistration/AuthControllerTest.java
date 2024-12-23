package ie.atu.taskmanagementregistration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import ie.atu.taskmanagementregistration.Authentication.AuthController;
import ie.atu.taskmanagementregistration.Authentication.AuthService;
import ie.atu.taskmanagementregistration.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private BCryptPasswordEncoder passEncoder;

    @Test
    @WithMockUser
    void successfulRegistration() throws Exception {
        User user = new User("123456", "james@atu.ie", "Strong@Password123", "James", "Student");

        when(passEncoder.encode("Strong@Password123")).thenReturn("encoded_password");

        user.setPassword("encoded_password");

        when(authService.register(any(User.class))).thenReturn(ResponseEntity.ok("Thank you for joining us James"));

        mockMvc.perform(post("/register").contentType("application/json")
                        .with(csrf())
                        .content("{ \"email\": \"james@atu.ie\", \"password\": \"Strong@Password123\", \"firstName\": \"James\", \"lastName\": \"Student\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string("Thank you for joining us James"));
    }
}
