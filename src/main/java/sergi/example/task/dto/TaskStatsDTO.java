package sergi.example.task.dto;

import lombok.Getter;
import lombok.Setter;
import sergi.example.model.HourMinute;

@Getter
@Setter
public class TaskStatsDTO {

    private Long id;
    private String name;
    private HourMinute hourMinute;

    public TaskStatsDTO(long id, String name, long duration) {
        this.id = id;
        this.name = name;
        hourMinute = new HourMinute(duration);
    }
}
