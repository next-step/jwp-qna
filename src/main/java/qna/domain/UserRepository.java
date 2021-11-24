package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import qna.domain.commons.Name;
import qna.domain.commons.UserId;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserId(String userId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update User u set u.name = :name where u.id = :id")
  void updateNameById(@Param("id") long id, @Param("name") Name name);

  Optional<User> findUserByUserId(UserId userId);
}
