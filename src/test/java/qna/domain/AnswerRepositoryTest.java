package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {
	@Autowired
	AnswerRepository answerRepository;

	private Answer A1;
	private Answer A2;

	@BeforeEach
	void setup() {
		A1 = answerRepository.save(AnswerTest.A1);
		A2 = answerRepository.save(AnswerTest.A2);
	}

	@Test
	void createdAt_updatedAt_반영_확인() {
		assertThat(A1.getCreatedAt()).isNotNull();
		assertThat(A1.getUpdatedAt()).isNotNull();
	}

	@Test
	void findByQuestionIdAndDeletedFalse() {
		List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
		actual.forEach(answer -> {
			assertThat(answer.getQuestionId()).isEqualTo(QuestionTest.Q1.getId());
			assertThat(answer.isDeleted()).isFalse();
		});
	}

	@Test
	void findByIdAndDeletedFalse() {
		Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(A1.getId());
		assertThat(actual).isEqualTo(Optional.of(A1));
	}
}
