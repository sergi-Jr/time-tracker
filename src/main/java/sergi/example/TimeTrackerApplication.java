package sergi.example;

import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import utils.InitData;

import java.util.Locale;

@SpringBootApplication
@EnableJpaAuditing
public class TimeTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimeTrackerApplication.class, args);
    }

    @Bean
    public static Faker getFaker() {
        return new Faker(new Locale("en", "US"));
    }

    @Bean
    public static InitData getInitData() {
        return new InitData();
    }
}
