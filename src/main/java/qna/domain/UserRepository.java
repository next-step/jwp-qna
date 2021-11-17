package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserDataUserId(String userId);

    List<User> findByUserDataName(String name);

    long countByUserDataUserId(String userId);
}
