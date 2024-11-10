package ie.atu.taskmanagementregistration.Authentication;

import ie.atu.taskmanagementregistration.User.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;
    private final BCryptPasswordEncoder passEncoder;

    public AuthController(AuthService authService, BCryptPasswordEncoder passEncoder) {
        this.authService = authService;
        this.passEncoder = passEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        user.setPassword(passEncoder.encode(user.getPassword()));
        return authService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginUser user) {
        return authService.login(user);
    }

}
