package qna.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    int countByName(String name);
}
