package ie.atu.taskmanagementregistration.Authentication;

import ie.atu.taskmanagementregistration.User.User;
import ie.atu.taskmanagementregistration.User.UserDB;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserDB userDB;
    private final BCryptPasswordEncoder passEncoder;

    public AuthService(UserDB userDB, BCryptPasswordEncoder passEncoder) {
        this.userDB = userDB;
        this.passEncoder = passEncoder;
    }

    public ResponseEntity<String> register(User user) {
        Optional<User> existingUserOptional = userDB.findByEmail(user.getEmail());
        if (existingUserOptional.isPresent()) {
            return ResponseEntity.status(401).body("User already exists");
        } else {
            userDB.save(user);
            return ResponseEntity.ok("Thank you for joining us " + user.getFirstName());
        }
    }

    public ResponseEntity<String> login(LoginUser user) {
        Optional<User> existingUserOptional = userDB.findByEmail(user.email);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            if (!passEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                return ResponseEntity.status(401).body("Password incorrect");
            }
            return ResponseEntity.ok("Welcome " + existingUser.getFirstName());
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
