package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.ContentType.QUESTION;

@DataJpaTest
class DeleteHistoryRepositoryTest {

  private final DeleteHistory givenHistory = new DeleteHistory(QUESTION, 1L, 1L, LocalDateTime.now());

  @Autowired
  private DeleteHistoryRepository deleteHistoryRepository;

  private DeleteHistory saved;

  @BeforeEach
  void setUp() {
    saved = deleteHistoryRepository.save(givenHistory);
  }

  @DisplayName("저장 후 반환 값은 원본과 같다.")
  @Test
  void saveTest() {
    assertThat(saved).isEqualTo(givenHistory);
  }
}
