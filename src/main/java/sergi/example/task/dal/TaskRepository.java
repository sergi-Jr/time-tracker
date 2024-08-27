package sergi.example.task.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sergi.example.task.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
