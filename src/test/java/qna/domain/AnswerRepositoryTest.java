package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
	private User javajigi;
	private User sanjigi;
	private Question question;
	private Answer expected1;
	private Answer expected2;

	@Autowired
	private AnswerRepository answerRepository;

	@BeforeEach
	void init(
		@Autowired final UserRepository userRepository,
		@Autowired final QuestionRepository questionRepository
	) {
		javajigi = userRepository.save(UserTest.JAVAJIGI);
		sanjigi = userRepository.save(UserTest.SANJIGI);
		question = questionRepository.save(QuestionTest.Q1);

		expected1 = new Answer(javajigi, question, "Answers Contents1");
		expected2 = new Answer(sanjigi, question, "Answers Contents2");
	}

	@Test
	@DisplayName("답변 등록")
	void save() {
		Answer actual = answerRepository.save(expected1);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContents()).isEqualTo(expected1.getContents())
		);
	}

	@Test
	@DisplayName("Question Id로 삭제되지 않은 답변 조회")
	void findByQuestionIdAndDeletedFalse() {
		Answer actual1  = answerRepository.save(expected1);
		Answer actual2  = answerRepository.save(expected2);

		assertThat(answerRepository.findByQuestionIdAndDeletedFalse(question.getId()))
			.containsExactly(actual1, actual2);
	}

	@Test
	@DisplayName("ID로 답변 삭제")
	void deleteById() {
		Answer actual1  = answerRepository.save(expected1);
		answerRepository.save(expected2);

		answerRepository.deleteById(actual1.getId());

		assertThat(answerRepository.findByQuestionIdAndDeletedFalse(question.getId()))
			.doesNotContain(actual1);
	}

	@Test
	@DisplayName("Answer Id로 삭제되지 않은 답변 조회")
	void findByIdAndDeletedFalse() {
		Answer actual1  = answerRepository.save(expected1);
		Answer actual2  = answerRepository.save(expected2);

		answerRepository.deleteById(actual2.getId());

		Optional<Answer> findAnswer1 = answerRepository.findByIdAndDeletedFalse(actual1.getId());
		Optional<Answer> findAnswer2 = answerRepository.findByIdAndDeletedFalse(actual2.getId());

		assertAll(
			() -> assertThat(findAnswer1.isPresent()).isTrue(),
			() -> assertThat(findAnswer2.isPresent()).isFalse()
		);
	}

	@Test
	@DisplayName("Answer 수정")
	void updateAnswer() {
		Answer expected = answerRepository.save(expected1);
		expected.setContents("Answer Updated");
		Answer actual = answerRepository.findById(expected.getId()).get();

		assertThat(actual.getContents()).isEqualTo("Answer Updated");
	}
}
