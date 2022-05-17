package qna.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import qna.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
}
