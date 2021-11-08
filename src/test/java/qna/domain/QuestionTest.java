package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class QuestionTest {
  public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
  public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

  @Autowired
  QuestionRepository questionRepository;
  @Autowired
  EntityManager entityManager;

  @AfterEach
  void tearDown() {
    questionRepository.deleteAll();
    entityManager
      .createNativeQuery("alter table question alter column `id` restart with 1")
      .executeUpdate();
  }

  @Test
  void save() {
    assertThat(questionRepository.save(Q1).getId()).isEqualTo(Q1.getId());
  }

  @Test
  void findById() {
    questionRepository.save(Q1);
    questionRepository.save(Q2);

    Question question = questionRepository.findById(1L).orElse(null);

    Assertions.assertAll(
      () -> assertThat(question.getId()).isEqualTo(Q1.getId()),
      () -> assertThat(question.getTitle()).isEqualTo(Q1.getTitle()),
      () -> assertThat(question.getContents()).isEqualTo(Q1.getContents())
    );
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
