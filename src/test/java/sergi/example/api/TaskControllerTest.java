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
import sergi.example.user.User;
import sergi.example.user.dal.UserRepository;
import utils.InitData;

import java.time.LocalDateTime;
import java.util.Optional;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InitData init;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void testStartTracking() throws Exception {
        Task task = init.task();
        taskRepository.save(task);

        var request = put("/v1/tasks/" + task.getId() + "/start-tracking");
        mockMvc.perform(request).andExpect(status().isOk());

        Optional<Task> opActual = taskRepository.findById(task.getId());
        assertThat(opActual).isNotNull();

        Task actual = opActual.get();
        assertThat(actual.getStartedAt()).isNotNull();
    }

    @Test
    @Order(2)
    void testEndTracking() throws Exception {
        Task task = init.task();
        taskRepository.save(task);

        var request = put("/v1/tasks/" + task.getId() + "/end-tracking");
        mockMvc.perform(request).andExpect(status().isOk());

        Optional<Task> opActual = taskRepository.findById(task.getId());
        assertThat(opActual).isNotNull();

        Task actual = opActual.get();
        assertThat(actual.getFinishedAt()).isNotNull();
    }

    @Test
    @Order(3)
    @Transactional
    void testShowUserTrack() throws Exception {
        User user = init.user();
        Task task = init.task();
        task.setStartedAt(LocalDateTime.parse("2024-10-10T11:53:32"));
        task.setFinishedAt(LocalDateTime.parse("2024-10-10T16:53:32"));
        taskRepository.save(task);
        user.addTask(task);
        userRepository.save(user);

        var request = get("/v1/tasks/" + user.getEmail())
                .param("start-period", "2024-10-10")
                .param("end-period", "2024-10-10");
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }
}
