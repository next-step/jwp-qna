package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class AnswerRepositoryTest {

  @Autowired
  private AnswerRepository answerRepository;

  @Test
  void save() {
    User writer = new User("jko", "1234", "jko", "junheee.ko@gmail.com");
    Question question = new Question("what is JPA?", "blah blah");
    Answer expected = new Answer(writer, question, "this is JPA");

    Answer actual = answerRepository.save(expected);

    assertAll(
        () -> assertNotNull(actual.getId()),
        () -> assertEquals(expected.getId(), actual.getId())
    );
  }
}