package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTest {
  public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
  public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

  @Autowired
  UserRepository userRepository;
  @Autowired
  EntityManager entityManager;

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    entityManager
      .createNativeQuery("alter table user alter column `id` restart with 1")
      .executeUpdate();
  }

  @Test
  void save() {
    assertThat(userRepository.save(JAVAJIGI)).isEqualTo(JAVAJIGI);
  }

  @Test
  void findByUserId() {
    userRepository.save(JAVAJIGI);
    userRepository.save(SANJIGI);

    Optional<User> actual = userRepository.findByUserId("javajigi");

    assertThat(actual.get()).isEqualTo(JAVAJIGI);
  }

  @Test
  void findById() {
    userRepository.save(JAVAJIGI);
    userRepository.save(SANJIGI);

    Optional<User> actual = userRepository.findById(2L);

    assertThat(actual.get()).isEqualTo(SANJIGI);
  }

  @Test
  void deleteAll() {
    userRepository.save(JAVAJIGI);
    userRepository.save(SANJIGI);

    assertThat(userRepository.count()).isEqualTo(2);

    userRepository.deleteAll();

    assertThat(userRepository.count()).isEqualTo(0);
  }

  @Test
  void deleteById() {
    userRepository.save(JAVAJIGI);
    userRepository.save(SANJIGI);

    assertThat(userRepository.count()).isEqualTo(2);

    userRepository.deleteById(1L);

    assertThat(userRepository.findById(1L).isPresent()).isFalse();
  }

  @Test
  void updateNameById() {
    userRepository.save(JAVAJIGI);
    userRepository.save(SANJIGI);

    userRepository.updateNameById(1L, "js");

    assertThat(userRepository.findById(1L).map(User::getName).get()).isEqualTo("js");
  }
}
