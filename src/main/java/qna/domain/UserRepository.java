package qna.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(UserId userId);

    Optional<User> findByName(Name name);

    Optional<User> findByEmail(Email email);
}
