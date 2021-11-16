package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {

  @Autowired
  private QuestionRepository questions;

  public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
  public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

  @Test
  void saveQuestion() {
    Question savedQuestion = questions.save(Q1);

    assertAll(
        () -> assertThat(savedQuestion.getId()).isNotNull(),
        () -> assertThat(savedQuestion.getContents()).isEqualTo(Q1.getContents())
    );
  }

  @Test
  void findByTitle() {
    Question savedQuestion = questions.save(Q1);
    Question foundQuestion = questions.findFirstByTitle(Q1.getTitle());

    assertAll(
        () -> assertThat(foundQuestion.getId()).isNotNull(),
        () -> assertThat(foundQuestion.getContents()).isEqualTo(savedQuestion.getContents())
    );
  }

  @Test
  void updateQuestion() {
    Question savedQuestion = questions.save(Q1);
    Question foundQuestion = questions.findByIdAndDeletedFalse(savedQuestion.getId()).get();
    foundQuestion.setDeleted(true);

    Optional<Question> foundQuestionById = questions.findById(savedQuestion.getId());
    Optional<Question> foundQuestionByIdAndDeletedFalse = questions
        .findByIdAndDeletedFalse(savedQuestion.getId());

    assertAll(
        () -> assertThat(foundQuestionById).isPresent(),
        () -> assertThat(foundQuestionByIdAndDeletedFalse).isNotPresent()
    );
  }

  @Test
  void deleteQuestion() {
    Question savedQuestion = questions.save(Q1);
    Question foundQuestion = questions.findById(savedQuestion.getId()).get();

    questions.delete(foundQuestion);

    Optional<Question> foundQuestionById = questions.findById(savedQuestion.getId());

    assertAll(
        () -> assertThat(foundQuestionById).isNotPresent()
    );
  }
}
