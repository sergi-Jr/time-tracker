package sergi.example.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TaskCreateDTO {
    @Size(min = 3, max = 32)
    @NotBlank
    private String name;

    private JsonNullable<String> description;

    private JsonNullable<Long> userId;
}
