package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AnswerTest {
  public static final User JAVAJIGI = UserTest.JAVAJIGI;
  public static final Answer A1 = new Answer(JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
  public static final Answer A2 = new Answer(JAVAJIGI, QuestionTest.Q1, "Answers Contents2");
  public static final Answer A3 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents3");

  @Autowired
  private AnswerRepository answerRepository;
  @Autowired
  private EntityManager entityManager;

  @AfterEach
  void tearDown() {
    answerRepository.deleteAll();
    entityManager
      .createNativeQuery("alter table answer alter column `id` restart with 1")
      .executeUpdate();
  }

  @Test
  void save() {
    Answer savedAnswer = answerRepository.save(A1);
    assertThat(savedAnswer.getId()).isEqualTo(A1.getId());
  }

  @Test
  void deleteById() {
    answerRepository.save(A1);

    answerRepository.delete(A1);

    assertThat(answerRepository.findById(1L).isPresent()).isFalse();
  }

  @Test
  void updateContentsById() {
    answerRepository.save(A1);
    answerRepository.save(A2);
    answerRepository.save(A3);

    int updateCount = answerRepository.updateContentsById(A3.getId(), "TEST");

    assertThat(updateCount).isEqualTo(1);
  }

  @Test
  void findById() {
    answerRepository.save(A1);
    Answer actual = answerRepository.save(A2);

    Optional<Answer> expected = answerRepository.findById(2L);

    assertThat(actual).isEqualTo(expected.orElseThrow(NullPointerException::new));
  }

  @Test
  void findAnswersByWriterId() {
    answerRepository.save(A1);
    answerRepository.save(A2);
    answerRepository.save(A3);
    List<Answer> answers = answerRepository.findByWriterId(UserTest.JAVAJIGI.getId());

    assertThat(answers.stream().map(Answer::getWriterId).collect(Collectors.toList()))
      .contains(JAVAJIGI.getId());
  }
}
