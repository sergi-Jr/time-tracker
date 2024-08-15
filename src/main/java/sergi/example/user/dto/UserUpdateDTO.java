package sergi.example.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserUpdateDTO {
    private JsonNullable<String> email;

    @Size(min = 3, max = 100)
    private JsonNullable<String> name;
}
