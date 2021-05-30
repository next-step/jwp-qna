package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class UserTest {
  public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
  public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

  @Autowired
  private UserRepository users;

  @Test
  public void save(){
    User javajigi = users.save(JAVAJIGI);

    assertAll(
      () -> assertThat(javajigi.getId()).isNotNull(),
      () -> assertThat(javajigi.getUserId()).isEqualTo(JAVAJIGI.getUserId())
    );
  }

  @Test
  public void findByUserId(){
    final Optional<User> byUserId = users.findByUserId(JAVAJIGI.getUserId());

    assertThat(byUserId).isNotEmpty();
  }
}
