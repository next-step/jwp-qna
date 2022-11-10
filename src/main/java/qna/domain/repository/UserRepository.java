package qna.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.user.User;
import qna.domain.user.name.Name;
import qna.domain.user.userid.UserId;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(UserId userId);

    int countByName(Name name);

    int countByUserId(UserId userId);
}
