package sergi.example.task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sergi.example.task.dal.TaskService;
import sergi.example.task.dto.TaskDTO;
import sergi.example.task.dto.TaskUpdateDTO;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService service) {
        taskService = service;
    }

    @PutMapping(path = "/{id}/start-tracking", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskDTO startTracking(@PathVariable Long id) {
        TaskUpdateDTO data = new TaskUpdateDTO();
        data.setStartedAt();
        return taskService.update(id, data);
    }

    @PutMapping(path = "/{id}/end-tracking", produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskDTO endTracking(@PathVariable Long id, @RequestBody TaskUpdateDTO data) {
        return taskService.update(id, data);
    }
}
