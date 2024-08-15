package sergi.example.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;

    private String name;

    private String description;

    private LocalDate startedAt;

    private LocalDate finishedAt;

    private Long userId;
}
