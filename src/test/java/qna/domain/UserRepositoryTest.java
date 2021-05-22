package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  private User saved;

  @BeforeEach
  void setUp() {
    saved = userRepository.save(JAVAJIGI);
  }

  @DisplayName("저장 후 반환 값은 원본과 같다.")
  @Test
  void saveTest() {
    assertAll(
        () -> assertThat(saved.getId()).isNotNull(),
        () -> assertThat(saved.getEmail()).isEqualTo(JAVAJIGI.getEmail()),
        () -> assertThat(saved.getName()).isEqualTo(JAVAJIGI.getName()),
        () -> assertThat(saved.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
        () -> assertThat(saved.getPassword()).isEqualTo(JAVAJIGI.getPassword())
    );
  }

  @DisplayName("userId 값으로 데이터를 조회할 수 있다.")
  @Test
  void findByUserIdTest() {
    assertThat(userRepository.findByUserId(saved.getUserId())).hasValue(saved);
  }
}
