package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.ContentType.QUESTION;
import static qna.domain.UserTest.JAVAJIGI;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class DeleteHistoryRepositoryTest {

  @Autowired
  private DeleteHistoryRepository deleteHistoryRepository;
  @Autowired
  private UserRepository userRepository;

  private User savedUser;

  @BeforeEach
  void setUp() {
    User toSaveUser = new User("ted", "password", "name", "enemfk777@gmail.com");
    savedUser = userRepository.save(toSaveUser);
  }

  @DisplayName("저장 후 반환 값은 원본과 같다.")
  @Test
  void saveTest() {
    //given
    DeleteHistory toSaveDeleteHistory = new DeleteHistory(QUESTION, 1L, savedUser, LocalDateTime.now());

    //when & then
    DeleteHistory saved = deleteHistoryRepository.save(toSaveDeleteHistory);
    assertThat(saved).isEqualTo(toSaveDeleteHistory);
  }
}
