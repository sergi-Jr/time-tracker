package sergi.example.api;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import sergi.example.task.Task;
import sergi.example.task.dal.TaskRepository;
import utils.InitData;

import java.util.Optional;

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
}
