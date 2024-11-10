package ie.atu.taskmanagementregistration.Authentication;

import ie.atu.taskmanagementregistration.User.User;
import ie.atu.taskmanagementregistration.User.UserDB;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDB userDB;

    public AuthService(UserDB userDB) {
        this.userDB = userDB;
    }

    public ResponseEntity<User> register(User user) {
        return ResponseEntity.ok(userDB.save(user));
    }

    public ResponseEntity<String> login(User user) {
        try {
            if (userDB.existsById(user.getId())) {
                return ResponseEntity.ok(user.getFirstName());
            } else {
                return ResponseEntity.ok("User does not exist");
            }
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

}
