package sergi.example.track.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sergi.example.model.HourMinute;
import sergi.example.track.dal.TrackService;
import sergi.example.track.dto.TrackDTO;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/track")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    @PutMapping(path = "/start-tracking")
    public void startTracking(@RequestParam Long userId, @RequestParam Long taskId) {
        trackService.startTrack(userId, taskId);
    }

    @PutMapping(path = "/end-tracking")
    public void endTracking(@RequestParam Long userId, @RequestParam Long taskId) {
        trackService.endTrack(userId, taskId);
    }

    @GetMapping(path = "/{username}/timespent")
    public List<TrackDTO> showUserTrack(@RequestParam(name = "start-period") LocalDate start,
                                            @RequestParam(name = "end-period") LocalDate end,
                                            @PathVariable String username) {
        return trackService.getUserTrackStats(username, start, end);
    }

    @GetMapping(path = "/{username}/all-timespent")
    public HourMinute showAllTimeSpent(@RequestParam(name = "start-period") LocalDate start,
                                       @RequestParam(name = "end-period") LocalDate end,
                                       @PathVariable String username) {
        return trackService.getAllTimeSpent(username, start, end);
    }

    @DeleteMapping(path = "/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStats(@PathVariable String username) {
        trackService.deleteUserStats(username);
    }
}
