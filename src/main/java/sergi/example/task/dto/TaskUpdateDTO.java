package sergi.example.task.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.sql.Timestamp;

@Getter
@Setter
public class TaskUpdateDTO {
    @Size(min = 3, max = 32)
    private String name;

    private JsonNullable<String> description;

    private JsonNullable<Long> userId;

    private JsonNullable<Timestamp> startedAt;

    private JsonNullable<Timestamp> finishedAt;
}
