package sergi.example.model;

import lombok.Getter;

@Getter
public final class HourMinute {
    private long hour;
    private long minute;

    public HourMinute(long duration) {
        hour = duration / 60;
        minute = duration % 60;
    }

    @Override
    public String toString() {
        return hour + ":" + minute;
    }
}
