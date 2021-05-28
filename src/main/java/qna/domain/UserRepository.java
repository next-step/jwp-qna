package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.wrap.UserId;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(UserId userId);
}
