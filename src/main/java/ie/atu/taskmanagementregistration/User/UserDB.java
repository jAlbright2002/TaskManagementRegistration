package ie.atu.taskmanagementregistration.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDB extends MongoRepository<User, String> {
}
