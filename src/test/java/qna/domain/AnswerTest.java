package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {

  @Autowired
  AnswerRepository answers;

  public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1,
      "Answers Contents1");
  public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1,
      "Answers Contents2");

  @Test
  void saveAnswer() {
    Answer savedAnswer = answers.save(A1);

    assertAll(
        ()-> assertThat(savedAnswer.getId()).isNotNull(),
        ()-> assertThat(savedAnswer.getContents()).isEqualTo(A1.getContents())
    );
  }

  @Test
  void findAnswerByWriterId() {
    Answer savedAnswer = answers.save(A1);
    Answer foundAnswer = answers.findAnswerByWriterId(A1.getWriterId());

    assertAll(
        ()-> assertThat(foundAnswer.getId()).isNotNull(),
        ()-> assertThat(foundAnswer.getQuestionId()).isEqualTo(savedAnswer.getQuestionId())
    );
  }

  @Test
  void updateAnswer() {
    Answer savedAnswer = answers.save(A1);
    Answer foundAnswer = answers.findById(savedAnswer.getId()).get();

    foundAnswer.setContents(A2.getContents());
    Optional<Answer> foundAnswerOptional = answers.findById(savedAnswer.getId());

    assertAll(
        ()-> assertThat(foundAnswerOptional).isPresent(),
        ()-> assertThat(foundAnswerOptional.get().getContents()).isEqualTo(A2.getContents())
    );
  }

  @Test
  void deleteAnswer() {
    Answer savedAnswer = answers.save(A1);
    answers.delete(savedAnswer);
    Optional<Answer> answerOptional = answers.findById(savedAnswer.getId());

    assertThat(answerOptional).isNotPresent();

  }

}
