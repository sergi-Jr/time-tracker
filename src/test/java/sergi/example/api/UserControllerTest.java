package sergi.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sergi.example.task.dal.TaskRepository;
import sergi.example.user.User;
import sergi.example.user.dal.UserRepository;
import sergi.example.user.dto.UserCreateDTO;
import utils.InitData;

import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InitData init;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @Order(1)
    void testCreate() throws Exception {
        User user = init.user();
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail(user.getEmail());
        dto.setName(JsonNullable.of(user.getName()));

        var request = post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));
        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Optional<User> opActual = userRepository.findUserByEmail(user.getEmail());
        assertThat(opActual).isNotNull();

        User actual = opActual.get();
        assertThat(user.getEmail()).isEqualTo(actual.getEmail());
        assertThat(user.getName()).isEqualTo(actual.getName());
    }

    @Test
    @Order(2)
    void testUpdate() throws Exception {
        User user = init.user();
        userRepository.save(user);

        Map<String, String> data = Map.of("email", "trueTest@example.com", "name", "John Doe");

        var request = put("/v1/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());

        Optional<User> opActual = userRepository.findById(user.getId());
        assertThat(opActual).isNotNull();

        User actual = opActual.get();
        assertThat(data)
                .containsEntry("email", actual.getEmail())
                .containsEntry("name", actual.getName());
    }
}
