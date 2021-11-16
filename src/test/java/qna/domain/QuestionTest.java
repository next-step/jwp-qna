package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import static org.springframework.test.annotation.DirtiesContext.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class QuestionTest {
  @Autowired
  QuestionRepository questionRepository;

  @Autowired
  EntityManager entityManager;

  @BeforeEach
  void setUp() {
    entityManager.createNativeQuery("ALTER TABLE `question` ALTER COLUMN `id` RESTART WITH 1")
      .executeUpdate();

    Question q1 = QuestionFactory.create("test", "contents");
    Question q2 = QuestionFactory.create("test q2", "contents q2");
  }

  @DisplayName("질문을 저장한다.")
  @Test
  void save() {
    Question q1 = QuestionFactory.create("test", "contents");
    Question q2 = QuestionFactory.create("test q2", "contents q2");
    Question actual = questionRepository.save(q1);

    assertAll(
      () -> assertThat(actual.getId()).isNotNull(),
      () -> assertThat(actual.getTitle()).isEqualTo(q1.getTitle()),
      () -> assertThat(actual.getContents()).isEqualTo(q1.getContents())
    );
  }

  @DisplayName("질문을 ID로 찾는다.")
  @Test
  void findById() {
    Question q1 = QuestionFactory.create("test", "contents");
    Question q2 = QuestionFactory.create("test q2", "contents q2");
    questionRepository.save(q1);

    Question question = questionRepository.findById(q1.getId()).orElse(null);

    assertAll(
      () -> assertThat(question.getId()).isNotNull(),
      () -> assertThat(question.getTitle()).isEqualTo(q1.getTitle()),
      () -> assertThat(question.getContents()).isEqualTo(q1.getContents())
    );
  }

  @DisplayName("모든 질문을 검색한다.")
  @Test
  void findAll() {
    Question q1 = QuestionFactory.create("test", "contents");
    Question q2 = QuestionFactory.create("test q2", "contents q2");
    questionRepository.save(q1);
    questionRepository.save(q2);

    assertThat(questionRepository.findAll().size()).isEqualTo(2);
  }

  @DisplayName("모든 질문을 삭제한다.")
  @Test
  void deleteAll() {
    Question q1 = QuestionFactory.create("test", "contents");
    Question q2 = QuestionFactory.create("test q2", "contents q2");
    questionRepository.save(q1);
    questionRepository.save(q2);

    assertThat(questionRepository.count()).isEqualTo(2);

    questionRepository.deleteAll();

    assertThat(questionRepository.count()).isEqualTo(0);
  }
}
