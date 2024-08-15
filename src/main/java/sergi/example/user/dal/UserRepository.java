package sergi.example.user.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sergi.example.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
