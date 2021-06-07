package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class QuestionRepositoryTest {

  @Autowired
  private QuestionRepository questionRepository;

  @Test
  void save() {
    Question expected = new Question("what is JPA?", "blah blah");

    Question actual = questionRepository.save(expected);

    assertAll(
        () -> assertNotNull(actual.getId()),
        () -> assertEquals(expected.getId(), actual.getId())
    );
  }
}