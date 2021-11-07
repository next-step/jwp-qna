package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
  public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
  public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

  @Autowired
  QuestionRepository questionRepository;

  @Test
  void save() {
    assertThat(questionRepository.save(Q1)).isEqualTo(Q1);
  }

  @Test
  void findById() {
    questionRepository.save(Q1);
    questionRepository.save(Q2);

    Optional<Question> question = questionRepository.findById(1L);

    assertThat(question.get()).isEqualTo(Q1);
  }

  @Test
  void findAll() {
    questionRepository.save(Q1);
    questionRepository.save(Q2);

    assertThat(questionRepository.findAll().size()).isEqualTo(2);
  }

  @Test
  void deleteAll() {
    questionRepository.save(Q1);
    questionRepository.save(Q2);

    assertThat(questionRepository.count()).isEqualTo(2L);

    questionRepository.deleteAll();

    assertThat(questionRepository.count()).isEqualTo(0L);
  }
}
