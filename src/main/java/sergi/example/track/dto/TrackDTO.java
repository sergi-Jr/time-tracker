package sergi.example.track.dto;

import lombok.Getter;
import lombok.Setter;
import sergi.example.model.HourMinute;

@Getter
@Setter
public class TrackDTO {
    private long taskId;
    private String taskName;
    private HourMinute timeSpent;
}
