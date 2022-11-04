package qna.domain.answer;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.TestConstructor;

import qna.domain.generator.AnswerGenerator;
import qna.domain.generator.QuestionGenerator;
import qna.domain.generator.UserGenerator;
import qna.domain.question.Question;
import qna.domain.user.User;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import({AnswerGenerator.class, QuestionGenerator.class, UserGenerator.class})
@DisplayName("Answer Repository 테스트")
class AnswerRepositoryTest {

	private final AnswerRepository answerRepository;
	private final AnswerGenerator answerGenerator;
	private final QuestionGenerator questionGenerator;
	private final UserGenerator userGenerator;
	private final EntityManager entityManager;

	public AnswerRepositoryTest(
		AnswerRepository answerRepository,
		AnswerGenerator answerGenerator,
		QuestionGenerator questionGenerator,
		UserGenerator userGenerator,
		EntityManager entityManager
	) {
		this.answerRepository = answerRepository;
		this.answerGenerator = answerGenerator;
		this.questionGenerator = questionGenerator;
		this.userGenerator = userGenerator;
		this.entityManager = entityManager;
	}

	@Test
	@DisplayName("답변 저장 테스트")
	void saveTest() {
		User questionWriter = userGenerator.savedUser(UserGenerator.questionWriter());
		Question question = questionGenerator.savedQuestion(questionWriter);
		User answerWriter = userGenerator.savedUser(UserGenerator.answerWriter());
		Answer answer = new Answer(answerWriter, question, "answer contents");

		Answer actual = answerRepository.save(answer);

		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.isDeleted()).isFalse(),
			() -> assertThat(actual.getQuestion()).isEqualTo(question),
			() -> assertThat(actual).isSameAs(answer)
		);
	}

	@Test
	@DisplayName("답변 저장 시, 작성자가 영속 상태가 아니면 예외 반환")
	void saveWithNotPersistedWriterTest() {
		// given
		Question question = questionGenerator.savedQuestion(userGenerator.savedUser(UserGenerator.questionWriter()));
		User answerWriter = UserGenerator.answerWriter();
		Answer given = AnswerGenerator.answer(answerWriter, question, "answer contents");

		// when
		assertThatThrownBy(() -> answerRepository.save(given))
			.isInstanceOf(InvalidDataAccessApiUsageException.class);
	}

	@Test
	@DisplayName("답변 저장 시, 질문이 영속 상태가 아니면 예외 반환")
	void saveWithNotPersistedQuestionTest() {
		// given
		User questionWriter = UserGenerator.questionWriter();
		Question question = QuestionGenerator.question(questionWriter);
		User answerWriter = userGenerator.savedUser(UserGenerator.answerWriter());
		Answer given = AnswerGenerator.answer(answerWriter, question, "answer contents");

		// when
		assertThatThrownBy(() -> answerRepository.save(given))
			.isInstanceOf(InvalidDataAccessApiUsageException.class);
	}

	@Test
	@DisplayName("답변 저장 후 조회 테스트")
	void findByIdTest() {
		User questionWriter = userGenerator.savedUser(UserGenerator.questionWriter());
		Question question = questionGenerator.savedQuestion(questionWriter);
		User answerWriter = userGenerator.savedUser(UserGenerator.answerWriter());
		Answer answer = answerGenerator.savedAnswer(answerWriter, question, "answer contents");

		Answer foundAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId())
			.orElseThrow(() -> new IllegalArgumentException("entity is not found"));

		assertAll(
			() -> assertThat(foundAnswer).isNotNull(),
			() -> assertThat(foundAnswer).isEqualTo(answer),
			() -> assertThat(foundAnswer).isSameAs(answer)
		);
	}

	@Test
	@DisplayName("삭제 여부 true 로 변경 후 답변 조회 테스트 - dirty checking")
	void findDeletedTest() {
		// given
		User questionWriter = userGenerator.savedUser(UserGenerator.questionWriter());
		Question question = questionGenerator.savedQuestion(questionWriter);
		User answerWriter = userGenerator.savedUser(UserGenerator.answerWriter());
		Answer answer = answerGenerator.savedAnswer(answerWriter, question, "answer contents");

		// when
		answer.setDeleted(true);
		entityManager.flush();

		// then
		assertThat(answer.isDeleted()).isTrue();
	}
}