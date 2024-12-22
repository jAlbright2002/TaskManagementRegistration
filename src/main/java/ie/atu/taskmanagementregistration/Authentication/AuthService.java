package ie.atu.taskmanagementregistration.Authentication;

import ie.atu.taskmanagementregistration.Config.JwtConfig;
import ie.atu.taskmanagementregistration.User.Notification;
import ie.atu.taskmanagementregistration.User.User;
import ie.atu.taskmanagementregistration.User.UserDB;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserDB userDB;
    private final BCryptPasswordEncoder passEncoder;
    private final JwtConfig jwt;
    private final RabbitTemplate rabbitTemplate;

    public AuthService(UserDB userDB, BCryptPasswordEncoder passEncoder, JwtConfig jwt, RabbitTemplate rabbitTemplate) {
        this.userDB = userDB;
        this.passEncoder = passEncoder;
        this.jwt = jwt;
        this.rabbitTemplate = rabbitTemplate;
    }

    public ResponseEntity<String> register(User user) {
        Notification notification = new Notification();
        Optional<User> existingUserOptional = userDB.findByEmail(user.getEmail());
        if (existingUserOptional.isPresent()) {
            notification.setActionType("FAIL_REG");
            notification.setEmail(user.getEmail());
            rabbitTemplate.convertAndSend("regSendNotificationQueue", notification);
            return ResponseEntity.status(401).body("User already exists");
        } else {
            userDB.save(user);
            notification.setActionType("SUCC_REG");
            notification.setEmail(user.getEmail());
            rabbitTemplate.convertAndSend("regSendNotificationQueue", notification);
            return ResponseEntity.ok("Thank you for joining us " + user.getFirstName());
        }
    }

    public ResponseEntity<String> login(LoginUser user) {
        Notification notification = new Notification();
        Optional<User> existingUserOptional = userDB.findByEmail(user.getEmail());
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            if (!passEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                notification.setActionType("FAIL_LOG");
                notification.setEmail(user.getEmail());
                rabbitTemplate.convertAndSend("logSendNotificationQueue", notification);
                return ResponseEntity.status(401).body("Password incorrect");
            }
            notification.setActionType("SUCC_LOG");
            notification.setEmail(user.getEmail());
            rabbitTemplate.convertAndSend("logSendNotificationQueue", notification);
            return ResponseEntity.ok("Welcome " + existingUser.getFirstName() +
                    "\nToken: " + jwt.generateToken(existingUser.getEmail()));
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @RabbitListener(queues = {"regRecNotificationQueue", "logRecNotificationQueue"})
    public void receiveNotification(Notification notification) {
        notification.setRead(true);
        System.out.println(notification);
    }
}
