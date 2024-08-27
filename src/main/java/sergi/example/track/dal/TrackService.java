package sergi.example.track.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sergi.example.exception.ResourceNotFoundException;
import sergi.example.model.HourMinute;
import sergi.example.task.Task;
import sergi.example.task.dal.TaskRepository;
import sergi.example.track.CompositeTrackKey;
import sergi.example.track.Track;
import sergi.example.track.dto.TrackDTO;
import sergi.example.user.dal.UserRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TrackMapper trackMapper;


    @Transactional
    public void startTrack(Long userId, Long taskId) {
        Task model = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }

        LocalDateTime currentTime = LocalDateTime.now();
        model.setStartedAt(currentTime);
        taskRepository.save(model);

        Track track = new Track();
        track.setTaskId(taskId);
        track.setUserId(userId);
        trackRepository.save(track);
    }

    @Transactional
    public void endTrack(Long userId, Long taskId) {
        Task model = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found: " + userId);
        }
        Track track = trackRepository
                .findById(new CompositeTrackKey(userId, taskId))
                .orElseThrow(() -> new ResourceNotFoundException("This task is not yet tracked!"));

        LocalDateTime currentTime = LocalDateTime.now();
        model.setFinishedAt(currentTime);
        taskRepository.save(model);

        long durationInMinutes = Duration.between(model.getStartedAt(), currentTime).toMinutes();
        track.setDuration(durationInMinutes);
        trackRepository.save(track);
    }

    public List<TrackDTO> getUserTrackStats(String username, LocalDate start, LocalDate end) {
        long userId = userRepository
                .findUserByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username))
                .getId();

        List<Track> userTrackStatsInMinutes = trackRepository
                .findTrackStatsByUserIdAndDateBetween(userId, start.atStartOfDay(), end.atTime(LocalTime.MAX));
        return userTrackStatsInMinutes
                .stream()
                .map(trackMapper::map)
                .toList();
    }

    public HourMinute getAllTimeSpent(String username, LocalDate start, LocalDate end) {
        long userId = userRepository
                .findUserByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username))
                .getId();

        return trackRepository
                .getAllTimeSpentByUserIdAndDateBetween(userId, start.atStartOfDay(), end.atTime(LocalTime.MAX));
    }

    @Transactional
    public void deleteUserStats(String username) {
        long userId = userRepository
                .findUserByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username))
                .getId();

        trackRepository.deleteAllByUserId(userId);
    }
}
