package qna.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.entity.User;
import qna.domain.vo.UserId;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserId(UserId userId);
}
