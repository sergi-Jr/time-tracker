package sergi.example.user.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import sergi.example.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
