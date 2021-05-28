package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

	@Autowired
	QuestionRepository repository;

	@Test
	void saveAndFind() {
		Question actual = repository.save(QuestionTest.Q1);
		Question expect = repository.findById(actual.getId()).get();
		assertThat(actual).isEqualTo(expect);
	}
}
