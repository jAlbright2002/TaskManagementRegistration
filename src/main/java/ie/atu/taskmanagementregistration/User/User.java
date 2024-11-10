package ie.atu.taskmanagementregistration.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Email(message = "Email invalid, must be structured as 'youremail@domain'")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$",
             message = "Password must have at least 8 characters, a symbol, one capital and small letter and one number")
    @NotEmpty(message = "Password must not be empty")
    private String password;

    @NotEmpty(message = "Name must not be empty")
    @Pattern(regexp = "^*[A-z].+", message = "Name must be longer than one character")
    private String firstName;

    @Pattern(regexp = "^(Student|Professional|Personal|Other)$",
             message = "Category must be one of Student, Professional, Personal, Other")
    private String userCategory;

}
