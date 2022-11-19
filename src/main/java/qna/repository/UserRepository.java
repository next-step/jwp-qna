package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.User;
import qna.domain.UserId;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(UserId userId);
}
