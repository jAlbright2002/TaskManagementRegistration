package ie.atu.taskmanagementregistration.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserDB extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
