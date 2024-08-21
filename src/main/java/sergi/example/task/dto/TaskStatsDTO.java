package sergi.example.task.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TaskStatsDTO {

    private Long id;
    private int diff;

    public LocalTime getTimeSpent() {
        int hours = diff / 60;
        int minutes = diff % 60;
        return LocalTime.of(hours, minutes);
    }
}
