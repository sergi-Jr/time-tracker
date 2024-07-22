package sergi.example.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserCreateDTO {
    @Size(min = 3, max = 32)
    private JsonNullable<String> name;

    @NotBlank
    @Email
    private String email;
}
