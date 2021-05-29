package qna.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Answer;
import qna.domain.QuestionTest;
import qna.domain.UserTest;

@DataJpaTest
public class AnswerRepositoryTest {
	@Autowired
	private AnswerRepository answers;

	@Test
	void save() {
		Answer expected = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer actual = answers.save(expected);
		assertThat(actual.getId()).isNotNull();
	}

	@Test
	void findByIdAndDeletedFalse() {
		Answer expected = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		answers.save(expected);

		Answer actual = answers.findByIdAndDeletedFalse(expected.getId()).orElse(null);
		assertThat(actual).isEqualTo(expected);

		expected.setDeleted(true);
		actual = answers.findByIdAndDeletedFalse(expected.getId()).orElse(null);
		assertThat(actual).isNull();
	}
}