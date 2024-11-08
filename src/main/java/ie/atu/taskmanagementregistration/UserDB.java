package ie.atu.taskmanagementregistration;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDB extends MongoRepository<User, String> {
}
