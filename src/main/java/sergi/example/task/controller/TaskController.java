package sergi.example.task.controller;

import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sergi.example.task.dal.TaskService;
import sergi.example.task.dto.TaskDTO;
import sergi.example.task.dto.TaskStatsDTO;
import sergi.example.task.dto.TaskUpdateDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping(path = "/{username}/timespent")
    public List<Pair<Long, LocalTime>> showUserTrack(@RequestParam(name = "start-period") LocalDate start,
                                                     @RequestParam(name = "end-period") LocalDate end,
                                                     @PathVariable String username) {
        List<TaskStatsDTO> stats = taskService.getTrackingStats(username, start, end);
        return stats.stream()
                .map(dto -> Pair.of(dto.getId(), dto.getTimeSpent()))
                .collect(Collectors.toCollection(LinkedList::new));

    }

    @GetMapping(path = "/{username}/worktime")
    public List<TaskDTO> showWorkTime(@RequestParam(name = "start-period") LocalDate start,
                                      @RequestParam(name = "end-period") LocalDate end,
                                      @PathVariable String username) {
        return taskService.getWorkTime(username, start, end);
    }
}
