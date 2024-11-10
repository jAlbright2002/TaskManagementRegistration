package ie.atu.taskmanagementregistration.Authentication;

import ie.atu.taskmanagementregistration.Config.JwtConfig;
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
    private final JwtConfig jwt;

    public AuthService(UserDB userDB, BCryptPasswordEncoder passEncoder, JwtConfig jwt) {
        this.userDB = userDB;
        this.passEncoder = passEncoder;
        this.jwt = jwt;
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
        Optional<User> existingUserOptional = userDB.findByEmail(user.getEmail());
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            if (!passEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                return ResponseEntity.status(401).body("Password incorrect");
            }
            return ResponseEntity.ok("Welcome " + existingUser.getFirstName() +
                    "\nToken: " + jwt.generateToken(existingUser.getEmail()));
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
