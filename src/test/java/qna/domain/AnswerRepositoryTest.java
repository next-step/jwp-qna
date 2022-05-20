package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
	@Autowired
	private AnswerRepository answerRepository;

	@Test
	@DisplayName("답변 등록")
	void save() {
		Answer expected = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer actual  = answerRepository.save(expected);

		assertAll(
			() -> assertThat(actual .getId()).isNotNull(),
			() -> assertThat(actual .getContents()).isEqualTo(expected.getContents())
		);
	}

	@Test
	@DisplayName("Question Id로 삭제되지 않은 답변 조회")
	void findByQuestionIdAndDeletedFalse() {
		Answer expected1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer expected2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

		Answer actual1  = answerRepository.save(expected1);
		Answer actual2  = answerRepository.save(expected2);

		assertThat(answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId()))
			.containsExactly(actual1, actual2);
	}

	@Test
	@DisplayName("ID로 답변 삭제")
	void deleteById() {
		Answer expected1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer expected2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

		Answer actual1  = answerRepository.save(expected1);
		answerRepository.save(expected2);

		answerRepository.deleteById(actual1.getId());

		assertThat(answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId()))
			.doesNotContain(actual1);
	}

	@Test
	@DisplayName("Answer Id로 삭제되지 않은 답변 조회")
	void findByIdAndDeletedFalse() {
		Answer actual1  = answerRepository.save(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));
		Answer actual2  = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2"));

		answerRepository.deleteById(actual2.getId());

		Optional<Answer> findAnswer1 = answerRepository.findByIdAndDeletedFalse(actual1.getId());
		Optional<Answer> findAnswer2 = answerRepository.findByIdAndDeletedFalse(actual2.getId());

		assertAll(
			() -> assertThat(findAnswer1.isPresent()).isTrue(),
			() -> assertThat(findAnswer2.isPresent()).isFalse()
		);
	}
}
