package sergi.example.task.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sergi.example.exception.ResourceNotFoundException;
import sergi.example.model.HourMinute;
import sergi.example.task.Task;
import sergi.example.task.dto.TaskDTO;
import sergi.example.task.dto.TaskStatsDTO;
import sergi.example.task.dto.TaskUpdateDTO;
import sergi.example.user.dal.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private TaskRepository taskRepository;
    private TaskMapper taskMapper;
    private UserRepository userRepository;

    @Autowired
    public TaskService(TaskMapper mapper, TaskRepository repository, UserRepository userRepository) {
        taskMapper = mapper;
        taskRepository = repository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TaskDTO update(Long id, TaskUpdateDTO data) {
        Task model = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        taskMapper.update(data, model);
        taskRepository.save(model);
        return taskMapper.map(model);
    }

    public List<TaskStatsDTO> getTrackingStats(String username, LocalDate start, LocalDate end) {
        long userId = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found, email: " + username)).getId();

        return taskRepository
                .findTasksStatsByUserIdAndDateBetween(userId, start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    public List<TaskDTO> getWorkTime(String username, LocalDate start, LocalDate end) {
        long userId = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found, email: " + username)).getId();

        List<Task> tasks = taskRepository
                .findTasksByUserIdAndDateBetween(userId, start.atStartOfDay(), end.atTime(LocalTime.MAX));
        return tasks.stream().map(taskMapper::map).toList();
    }

    public HourMinute getAllTimeSpent(String username, LocalDate start, LocalDate end) {
        long userId = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found, email: " + username)).getId();
        return taskRepository
                .getTasksTimeSpentInMinutesByUserId(userId, start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    public LocalDateTime getTaskStartTimeByTaskId(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id)).getStartedAt();
    }
}
