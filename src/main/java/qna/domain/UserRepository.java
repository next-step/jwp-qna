package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import javax.swing.text.html.Option;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

	Optional<User> findByName(String userId);
}
