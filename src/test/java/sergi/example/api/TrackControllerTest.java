package sergi.example.api;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sergi.example.task.Task;
import sergi.example.task.dal.TaskRepository;
import sergi.example.track.CompositeTrackKey;
import sergi.example.track.Track;
import sergi.example.track.dal.TrackRepository;
import sergi.example.user.User;
import sergi.example.user.dal.UserRepository;
import utils.InitData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class TrackControllerTest {

    @Autowired
    private InitData init;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    @Transactional
    void testStartTracking() throws Exception {
        Task task = init.task();
        User user = init.user();
        taskRepository.save(task);
        user.addTask(task);
        userRepository.save(user);

        var request = put("/v1/track/start-tracking")
                .param("userId", String.valueOf(user.getId()))
                .param("taskId", String.valueOf(task.getId()));
        mockMvc.perform(request).andExpect(status().isOk());

        Optional<Task> opTaskActual = taskRepository.findById(task.getId());
        Optional<Track> opTrackActual = trackRepository
                .findById(new CompositeTrackKey(user.getId(), task.getId()));

        assertThat(opTaskActual).isNotNull();
        assertThat(opTrackActual).isNotNull();

        Task taskActual = opTaskActual.get();

        assertThat(taskActual.getStartedAt()).isNotNull();
    }

    @Test
    @Order(2)
    @Transactional
    void testEndTracking() throws Exception {
        Task task = init.task();
        task.setStartedAt(LocalDateTime.now().plusMinutes(10));
        taskRepository.save(task);

        User user = init.user();
        user.addTask(task);
        userRepository.save(user);

        Track track = new Track();
        track.setTaskId(task.getId());
        track.setUserId(user.getId());
        trackRepository.save(track);

        var request = put("/v1/track/end-tracking")
                .param("userId", String.valueOf(user.getId()))
                .param("taskId", String.valueOf(task.getId()));
        mockMvc.perform(request).andExpect(status().isOk());

        Optional<Task> opTaskActual = taskRepository.findById(task.getId());
        Optional<Track> opTrackActual = trackRepository
                .findById(new CompositeTrackKey(user.getId(), task.getId()));

        assertThat(opTaskActual).isNotNull();
        assertThat(opTrackActual).isNotNull();

        Task taskActual = opTaskActual.get();
        Track trackActual = opTrackActual.get();

        assertThat(taskActual.getFinishedAt()).isNotNull();
        assertThat(trackActual.getDuration()).isNotZero();
    }

    @Test
    @Order(3)
    @Transactional
    void testShowUserTrack() throws Exception {
        Task task = init.task();
        task.setStartedAt(LocalDateTime.parse("2024-10-10T10:01:00"));
        task.setFinishedAt(LocalDateTime.parse("2024-10-10T15:01:00"));
        taskRepository.save(task);

        User user = init.user();
        user.addTask(task);
        userRepository.save(user);

        Track track = new Track();
        track.setUserId(user.getId());
        track.setTaskId(task.getId());
        track.setDuration(300);
        trackRepository.save(track);

        var request = get("/v1/track/" + user.getEmail() + "/timespent")
                .param("start-period", "2024-10-10")
                .param("end-period", "2024-10-10");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();

        assertThatJson(body).isArray().hasSize(1);
    }

    @Test
    @Order(4)
    @Transactional
    void testShowAllTimeSpent() throws Exception {
        Task task = init.task();
        task.setStartedAt(LocalDateTime.parse("2024-10-10T10:01:00"));
        task.setFinishedAt(LocalDateTime.parse("2024-10-10T15:01:00"));
        taskRepository.save(task);
        Task task2 = init.task();
        task2.setStartedAt(LocalDateTime.parse("2024-11-11T10:01:00"));
        task2.setFinishedAt(LocalDateTime.parse("2024-11-11T15:01:00"));
        taskRepository.save(task2);

        User user = init.user();
        user.addTask(task);
        user.addTask(task2);
        userRepository.save(user);

        Track track = new Track();
        track.setUserId(user.getId());
        track.setTaskId(task.getId());
        track.setDuration(300);
        trackRepository.save(track);
        Track track2 = new Track();
        track2.setUserId(user.getId());
        track2.setTaskId(task2.getId());
        track2.setDuration(300);
        trackRepository.save(track2);

        var request = get("/v1/track/" + user.getEmail() + "/all-timespent")
                .param("start-period", "2024-10-10")
                .param("end-period", "2024-11-11");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("hour").isEqualTo(10),
                v -> v.node("minute").isEqualTo(0)
        );
    }

    @Test
    @Order(5)
    @Transactional
    void testDeleteStats() throws Exception {
        Task task = init.task();
        task.setStartedAt(LocalDateTime.parse("2024-10-10T10:01:00"));
        task.setFinishedAt(LocalDateTime.parse("2024-10-10T15:01:00"));
        taskRepository.save(task);
        Task task2 = init.task();
        task2.setStartedAt(LocalDateTime.parse("2024-11-11T10:01:00"));
        task2.setFinishedAt(LocalDateTime.parse("2024-11-11T15:01:00"));
        taskRepository.save(task2);

        User user = init.user();
        user.addTask(task);
        user.addTask(task2);
        userRepository.save(user);

        Track track = new Track();
        track.setUserId(user.getId());
        track.setTaskId(task.getId());
        track.setDuration(300);
        Track track2 = new Track();
        track2.setUserId(user.getId());
        track2.setTaskId(task2.getId());
        track2.setDuration(300);
        trackRepository.save(track2);

        var request = delete("/v1/track/" + user.getEmail());
        mockMvc.perform(request).andExpect(status().isNoContent());

        List<Track> tracks = trackRepository.findAllByUserId(user.getId());

        assertThat(tracks).isEmpty();
    }
}
