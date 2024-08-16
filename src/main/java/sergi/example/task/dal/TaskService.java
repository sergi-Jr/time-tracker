package sergi.example.task.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sergi.example.exception.ResourceNotFoundException;
import sergi.example.task.Task;
import sergi.example.task.dto.TaskDTO;
import sergi.example.task.dto.TaskUpdateDTO;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskMapper mapper, TaskRepository repository) {
        taskMapper = mapper;
        taskRepository = repository;
    }

    @Transactional
    public TaskDTO update(Long id, TaskUpdateDTO data) {
        Task model = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        taskMapper.update(data, model);
        taskRepository.save(model);
        return taskMapper.map(model);
    }
}
