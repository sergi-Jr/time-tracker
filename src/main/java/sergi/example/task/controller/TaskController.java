package sergi.example.task.controller;

import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sergi.example.model.HourMinute;
import sergi.example.task.dal.TaskService;
import sergi.example.task.dto.TaskDTO;
import sergi.example.task.dto.TaskStatsDTO;
import sergi.example.task.dto.TaskUpdateDTO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PutMapping(path = "/{id}/start-tracking")
    public TaskDTO startTracking(@PathVariable Long id) {
        TaskUpdateDTO data = new TaskUpdateDTO();
        LocalDateTime currentTime = LocalDateTime.now();
        data.setStartedAt(JsonNullable.of(currentTime));
        return taskService.update(id, data);
    }

    @PutMapping(path = "/{id}/end-tracking")
    public TaskDTO endTracking(@PathVariable Long id) {
        TaskUpdateDTO data = new TaskUpdateDTO();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime taskStartTrackTime = taskService.getTaskStartTimeByTaskId(id);
        long durationInMinutes = Duration.between(taskStartTrackTime, currentTime).toMinutes();

        data.setDuration(JsonNullable.of(durationInMinutes));
        data.setFinishedAt(JsonNullable.of(currentTime));
        return taskService.update(id, data);
    }

    @GetMapping(path = "/{username}/timespent")
    public List<TaskStatsDTO> showUserTrack(@RequestParam(name = "start-period") LocalDate start,
                                            @RequestParam(name = "end-period") LocalDate end,
                                            @PathVariable String username) {
        return taskService.getTrackingStats(username, start, end);
    }

    @GetMapping(path = "/{username}/worktime")
    public List<TaskDTO> showWorkTime(@RequestParam(name = "start-period") LocalDate start,
                                      @RequestParam(name = "end-period") LocalDate end,
                                      @PathVariable String username) {
        return taskService.getWorkTime(username, start, end);
    }

    @GetMapping(path = "/{username}/all-timespent")
    public HourMinute showAllTimeSpent(@RequestParam(name = "start-period") LocalDate start,
                                       @RequestParam(name = "end-period") LocalDate end,
                                       @PathVariable String username) {
        return taskService.getAllTimeSpent(username, start, end);
    }
}
