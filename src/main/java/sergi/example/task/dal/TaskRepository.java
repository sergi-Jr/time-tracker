package sergi.example.task.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sergi.example.model.HourMinute;
import sergi.example.task.Task;
import sergi.example.task.dto.TaskStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            select new sergi.example.task.dto.TaskStatsDTO(t.id, t.name, t.duration)
            from Task t
            where t.user.id = :userId
            and t.startedAt between :start and :end
            and t.finishedAt between :start and :end
            order by t.duration desc
            """)
    List<TaskStatsDTO> findTasksStatsByUserIdAndDateBetween(long userId, LocalDateTime start, LocalDateTime end);

    @Query("""
            select t from Task t
            where t.user.id = :userId
            and t.startedAt between :start and :end
            and t.finishedAt between :start and :end
            """)
    List<Task> findTasksByUserIdAndDateBetween(long userId, LocalDateTime start, LocalDateTime end);

    @Query("""
            select new sergi.example.model.HourMinute(SUM(t.duration))
            from Task t
            where t.user.id = :userId
            and t.startedAt between :start and :end
            and t.finishedAt between :start and :end
            """)
    HourMinute getTasksTimeSpentInMinutesByUserId(long userId, LocalDateTime start, LocalDateTime end);
}
