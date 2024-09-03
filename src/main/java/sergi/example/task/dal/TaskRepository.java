package sergi.example.task.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sergi.example.task.Task;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t.id from Task t where t.finishedAt is null")
    List<Long> findUnfinishedTasksIds();

    @Modifying
    @Query("update Task t set t.finishedAt = :dateTime where t.finishedAt is null and t.user.id is not null")
    void setFinishedAt(LocalDateTime dateTime);
}
