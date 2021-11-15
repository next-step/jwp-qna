package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserTest {
  public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
  public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

  @Autowired
  UserRepository userRepository;
  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void reset() {
    entityManager.createNativeQuery("ALTER TABLE `user` ALTER COLUMN `id` RESTART WITH 1")
      .executeUpdate();
  }

  @DisplayName("사용자를 저장한다.")
  @Test
  void save() {
    assertThat(userRepository.save(JAVAJIGI)).isEqualTo(JAVAJIGI);
  }

  @DisplayName("사용자를 사용자아이디로 조회한다.")
  @Test
  void findByUserId() {
    userRepository.save(JAVAJIGI);
    userRepository.save(SANJIGI);

    Optional<User> actual = userRepository.findByUserId("javajigi");

    assertThat(actual.get()).isEqualTo(JAVAJIGI);
  }

  @DisplayName("사용자를 식별자로 조회한다.")
  @Test
  void findById() {
    userRepository.save(JAVAJIGI);
    userRepository.save(SANJIGI);

    Optional<User> actual = userRepository.findById(SANJIGI.getId());

    assertThat(actual.get()).isEqualTo(SANJIGI);
  }

  @DisplayName("사용자를 모두 삭제한다.")
  @Test
  void deleteAll() {
    userRepository.save(JAVAJIGI);
    userRepository.save(SANJIGI);

    assertThat(userRepository.count()).isEqualTo(2);

    userRepository.deleteAll();

    assertThat(userRepository.count()).isEqualTo(0);
  }

  @DisplayName("사용자를 식별자로 삭제한다.")
  @Test
  void deleteById() {
    userRepository.save(JAVAJIGI);
    userRepository.save(SANJIGI);

    assertThat(userRepository.count()).isEqualTo(2);

    userRepository.deleteById(JAVAJIGI.getId());

    assertThat(userRepository.findById(JAVAJIGI.getId()).isPresent()).isFalse();
  }

  @DisplayName("사용자의 이름을 식별자로 수정한다.")
  @Test
  void updateNameById() {
    userRepository.save(JAVAJIGI);
    userRepository.save(SANJIGI);

    userRepository.updateNameById(JAVAJIGI.getId(), "js");

    assertThat(userRepository.findById(JAVAJIGI.getId()).map(User::getName).get()).isEqualTo("js");
  }
}
