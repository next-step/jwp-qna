package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;

@DataJpaTest
class AnswerRepositoryTest {

  @Autowired
  private AnswerRepository answerRepository;

  private Answer saved;

  @BeforeEach
  void setUp() {
    saved = answerRepository.save(A1);
  }

  @DisplayName("저장 후 반환 값은 원본과 같다.")
  @Test
  void saveTest() {
    assertAll(
        () -> assertThat(saved.getId()).isNotNull(),
        () -> assertThat(saved.getWriterId()).isEqualTo(A1.getWriterId()),
        () -> assertThat(saved.getContents()).isEqualTo(A1.getContents()),
        () -> assertThat(saved.getQuestionId()).isEqualTo(A1.getQuestionId())
    );
  }

  @DisplayName("deleted가 false인 데이터는 id로 조회할 수 있다.")
  @Test
  void findByIdAndDeletedFalseTest() {

    assertAll(
        () -> answerRepository.findByIdAndDeletedFalse(saved.getId())
                    .ifPresent(answer -> assertThat(answer).isEqualTo(saved)),
        () -> saved.setDeleted(true),
        () -> answerRepository.save(saved),
        () -> assertThat(answerRepository.findByIdAndDeletedFalse(saved.getId())).isNotPresent()
    );
  }

  @DisplayName("deleted가 false인 데이터는 연관된 QuestionId로 조회하면 응답에 포함되어 있다.")
  @Test
  void findByQuestionIdAndDeletedFalseTest() {
    //given
    Question givenQuestion = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    givenQuestion.setId(1L);
    Answer givenAnswer = new Answer(UserTest.JAVAJIGI, givenQuestion, "Answers Contents1");
    Answer savedAnswer = answerRepository.save(givenAnswer);

    assertAll(
        () -> assertThat(answerRepository.findByQuestionIdAndDeletedFalse(givenAnswer.getQuestionId())).contains(savedAnswer),
        () -> savedAnswer.setDeleted(true),
        () -> answerRepository.save(savedAnswer),
        () -> assertThat(answerRepository.findByQuestionIdAndDeletedFalse(givenAnswer.getQuestionId())).isEmpty()
    );
  }

}
