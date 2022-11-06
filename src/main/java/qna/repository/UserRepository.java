package qna.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.User;
import qna.domain.UserId;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(UserId userId);
}
