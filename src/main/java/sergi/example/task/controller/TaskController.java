package sergi.example.task.controller;

import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sergi.example.task.dal.TaskService;
import sergi.example.task.dto.TaskDTO;
import sergi.example.task.dto.TaskUpdateDTO;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PutMapping(path = "/{id}/start-tracking", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskDTO startTracking(@PathVariable Long id) {
        TaskUpdateDTO data = new TaskUpdateDTO();
        LocalDateTime currentTime = LocalDateTime.now();
        data.setStartedAt(JsonNullable.of(currentTime));
        return taskService.update(id, data);
    }

    @PutMapping(path = "/{id}/end-tracking", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskDTO endTracking(@PathVariable Long id) {
        TaskUpdateDTO data = new TaskUpdateDTO();
        LocalDateTime currentTime = LocalDateTime.now();
        data.setFinishedAt(JsonNullable.of(currentTime));
        return taskService.update(id, data);
    }
}
