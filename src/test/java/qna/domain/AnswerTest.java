package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
	@Autowired
	private AnswerRepository answerRepository;

	@Test
	void save() {
		Answer actual = answerRepository.save(A1);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContents()).isEqualTo(A1.getContents())
		);
	}

	@Test
	void findByQuestionIdAndDeletedFalse() {
		answerRepository.save(A1);
		final List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(
			A1.getQuestionId());

		assertThat(actual.get(0)).isEqualTo(A1);
	}
}
