package sergi.example.task.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sergi.example.task.Task;
import sergi.example.task.dto.TaskStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(name = "stats_native_query_dto", nativeQuery = true)
    List<TaskStatsDTO> findTasksStatsByUserIdAndDateBetween(@Param("userId") long userId,
                                                            @Param("start") LocalDateTime start,
                                                            @Param("end") LocalDateTime end);
}
