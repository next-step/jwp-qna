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
	private Answer javajigiAnswerExpected;
	private Answer sanjigiAnswerExpected;

	@Autowired
	private AnswerRepository answerRepository;

	@BeforeEach
	void init(
		@Autowired final UserRepository userRepository,
		@Autowired final QuestionRepository questionRepository
	) {
		javajigi = userRepository.save(UserTest.JAVAJIGI);
		sanjigi = userRepository.save(UserTest.SANJIGI);
		question = questionRepository.save(QuestionTest.TITLEA_QUESTION);

		javajigiAnswerExpected = new Answer(javajigi, question, "Answers Contents1");
		sanjigiAnswerExpected = new Answer(sanjigi, question, "Answers Contents2");
	}

	@Test
	@DisplayName("답변 등록")
	void save() {
		Answer actual = answerRepository.save(javajigiAnswerExpected);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContents()).isEqualTo(javajigiAnswerExpected.getContents())
		);
	}

	@Test
	@DisplayName("Question Id로 삭제되지 않은 답변 조회")
	void findByQuestionIdAndDeletedFalse() {
		Answer javajigiActual  = answerRepository.save(javajigiAnswerExpected);
		Answer sanjigiActual  = answerRepository.save(sanjigiAnswerExpected);

		assertThat(answerRepository.findByQuestionIdAndDeletedFalse(question.getId()))
			.containsExactly(javajigiActual, sanjigiActual);
	}

	@Test
	@DisplayName("ID로 답변 삭제")
	void deleteById() {
		Answer javajigiActual  = answerRepository.save(javajigiAnswerExpected);
		answerRepository.save(sanjigiAnswerExpected);

		answerRepository.deleteById(javajigiActual.getId());

		assertThat(answerRepository.findByQuestionIdAndDeletedFalse(question.getId()))
			.doesNotContain(javajigiActual);
	}

	@Test
	@DisplayName("Answer Id로 삭제되지 않은 답변 조회")
	void findByIdAndDeletedFalse() {
		Answer javajigiActual  = answerRepository.save(javajigiAnswerExpected);
		Answer sanjigiActual  = answerRepository.save(sanjigiAnswerExpected);

		answerRepository.deleteById(sanjigiActual.getId());

		Optional<Answer> findAnswer1 = answerRepository.findByIdAndDeletedFalse(javajigiActual.getId());
		Optional<Answer> findAnswer2 = answerRepository.findByIdAndDeletedFalse(sanjigiActual.getId());

		assertAll(
			() -> assertThat(findAnswer1.isPresent()).isTrue(),
			() -> assertThat(findAnswer2.isPresent()).isFalse()
		);
	}

	@Test
	@DisplayName("Answer 수정")
	void updateAnswer() {
		Answer expected = answerRepository.save(javajigiAnswerExpected);
		expected.setContents("Answer Updated");
		Answer actual = answerRepository.findById(expected.getId()).get();

		assertThat(actual.getContents()).isEqualTo("Answer Updated");
	}
}
