package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;

@DataJpaTest
class QuestionRepositoryTest {

  @Autowired
  private QuestionRepository questionRepository;

  private Question saved;

  @BeforeEach
  void setUp() {
    saved = questionRepository.save(Q1);
  }

  @DisplayName("저장 후 반환 값은 원본과 같다.")
  @Test
  void saveTest() {
    assertAll(
        () -> assertThat(saved.getId()).isNotNull(),
        () -> assertThat(saved.getWriterId()).isEqualTo(Q1.getWriterId()),
        () -> assertThat(saved.getContents()).isEqualTo(Q1.getContents()),
        () -> assertThat(saved.getTitle()).isEqualTo(Q1.getTitle())
    );
  }

  @DisplayName("deleted가 false인 데이터는 id로 조회할 수 있다.")
  @Test
  void findByIdAndDeletedFalseTest() {
    assertAll(
        () -> assertThat(questionRepository.findByIdAndDeletedFalse(saved.getId())).hasValue(saved),
        () -> saved.setDeleted(true),
        () -> questionRepository.save(saved),
        () -> assertThat(questionRepository.findByIdAndDeletedFalse(saved.getId())).isNotPresent()
    );
  }

  @DisplayName("deleted가 false인 데이터는 연관된 QuestionId로 조회하면 응답에 포함되어 있다.")
  @Test
  void findByDeletedFalseTest() {
    assertAll(
        () -> assertThat(questionRepository.findByDeletedFalse()).contains(saved),
        () -> saved.setDeleted(true),
        () -> questionRepository.save(saved),
        () -> assertThat(questionRepository.findByDeletedFalse()).isEmpty()
    );
  }

}
