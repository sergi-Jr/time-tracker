package sergi.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sergi.example.user.User;
import sergi.example.user.dal.UserRepository;
import sergi.example.user.dto.UserCreateDTO;

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
    private Faker faker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    private User generateUser() {
        return Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .ignore(Select.field(User::getTasks))
                .supply(Select.field(User::getName), () -> faker.name().name())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
    }

    @Test
    @Order(1)
    void testCreate() throws Exception {
        User user = generateUser();
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail(user.getEmail());
        dto.setName(JsonNullable.of(user.getName()));

        var request = post("/users")
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
    //TODO fix this test
    void testUpdate() throws Exception {
        User user = generateUser();
        userRepository.save(user);

        Map<String, String> data = Map.of("email", "trueTest@example.com", "name", "John Doe");

        var request = put("/users/" + user.getId())
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