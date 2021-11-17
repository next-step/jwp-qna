package qna.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import qna.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserAuthUserId(String userId);

    List<User> findByUserDataName(String name);

    long countByUserAuthUserId(String userId);
}
