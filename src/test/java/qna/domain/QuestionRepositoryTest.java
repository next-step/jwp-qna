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

	@Autowired
	UserRepository userRepository;

	@Test
	void saveAndFind() {
		User user = userRepository.save(UserTest.JAVAJIGI);
		Question q1 = QuestionTest.Q1.writeBy(user);
		Question actual = repository.save(q1);
		Question expect = repository.findById(actual.getId()).orElse(Question.NONE);

		assertThat(actual).isEqualTo(expect);
	}
}
