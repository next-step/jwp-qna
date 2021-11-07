package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserId(String userId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update User u set u.name = ?2 where u.id = ?1")
  void updateNameById(long id, String name);
}
