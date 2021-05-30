package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
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

	@Test
	@DisplayName("Deleted 가 False 인 모든 Question 조회")
	void findByDeletedFalseTest() {
		User user = userRepository.save(UserTest.JAVAJIGI);
		Question q1 = QuestionTest.Q1.writeBy(user);
		Question q2 = QuestionTest.Q2.writeBy(user);
		q1.setDeleted(false);
		q2.setDeleted(true);
		repository.save(q1);
		repository.save(q2);

		List<Question> questions = repository.findByDeletedFalse();
		assertThat(questions).containsExactly(q1);
	}

	@Test
	@DisplayName("Deleted 가 False 인 특정 Question 조회")
	void findByIdAndDeletedFalseTest() {
		User sanjigi = UserTest.SANJIGI;
		User save = userRepository.save(sanjigi);

		Question q1 = QuestionTest.Q1.writeBy(save);
		q1.setDeleted(false);
		Question save1 = repository.save(q1);

		Question question = repository.findByIdAndDeletedFalse(save1.getId()).orElse(Question.NONE);
		assertThat(question).isEqualTo(save1);

		Question q2 = QuestionTest.Q1.writeBy(save);
		q2.setDeleted(true);
		Question save2 = repository.save(q2);

		Question question2 = repository.findByIdAndDeletedFalse(save2.getId()).orElse(Question.NONE);
		assertThat(question2).isEqualTo(Question.NONE);
	}
}
