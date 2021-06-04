package qna.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.junit.jupiter.api.Test;

@DataJpaTest
class QuestionRepositoryTest {

  @Autowired
  private QuestionRepository questionRepository;

  @Test
  void name() {

  }
}