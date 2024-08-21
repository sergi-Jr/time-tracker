package sergi.example.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDTO {
    private Long id;

    private String name;

    private String description;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private Long userId;
}
