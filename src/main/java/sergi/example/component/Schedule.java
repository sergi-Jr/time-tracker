package sergi.example.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sergi.example.task.dal.TaskRepository;
import sergi.example.track.dal.TrackRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@EnableScheduling
public class Schedule {

    private TrackRepository trackRepository;
    private TaskRepository taskRepository;


    @Autowired
    public Schedule(TrackRepository trackRepository, TaskRepository taskRepository) {
        this.trackRepository = trackRepository;
        this.taskRepository = taskRepository;
    }

    @Scheduled(cron = "0 59 23 * * *")
    public void setDefaultTaskFinishTime() {
        LocalDateTime combinedDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        taskRepository.setFinishedAt(combinedDateTime);
        trackRepository.setDuration();
    }
}
