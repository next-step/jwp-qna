package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void save() {
    User expected = new User("jko", "1234", "jko", "junheee.ko@gmail.com");

    User actual = userRepository.save(expected);

    assertAll(
        () -> assertNotNull(actual.getId()),
        () -> assertEquals(expected.getId(), actual.getId())
    );
  }
}