package utils;

import net.datafaker.Faker;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sergi.example.task.Task;
import sergi.example.user.User;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

@Component
public class InitData {

    @Autowired
    private Faker faker;

    public User user() {
        return Instancio.of(User.class)
                .ignore(all(
                        field(User::getId),
                        field(User::getCreatedAt),
                        field(User::getUpdatedAt),
                        field(User::getTasks)
                ))
                .supply(field(User::getName), () -> faker.name().name())
                .supply(field(User::getEmail), () -> faker.internet().emailAddress())
                .create();
    }

    public Task task() {
        return Instancio.of(Task.class)
                .ignore(all(
                        field(Task::getStartedAt),
                        field(Task::getId),
                        field(Task::getUser),
                        field(Task::getDescription),
                        field(Task::getCreatedAt),
                        field(Task::getUpdatedAt),
                        field(Task::getFinishedAt)
                ))
                .supply(field(Task::getName), () -> faker.lorem().word())
                .create();
    }
}
