package ie.atu.taskmanagementregistration;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserDB userDB;

    public AuthService(UserDB userDB) {
        this.userDB = userDB;
    }

    public User register(User user) {
        return (userDB.save(user));
    }

}
