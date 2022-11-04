package qna.domain.deletehistory;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestConstructor;

import qna.domain.ContentType;
import qna.domain.answer.Answer;
import qna.domain.generator.AnswerGenerator;
import qna.domain.generator.QuestionGenerator;
import qna.domain.generator.UserGenerator;
import qna.domain.question.Question;
import qna.domain.user.User;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import({UserGenerator.class, QuestionGenerator.class, AnswerGenerator.class})
@DisplayName("DeleteHistory Repository 테스트")
class DeleteHistoryRepositoryTest {

	private final DeleteHistoryRepository deleteHistoryRepository;
	private final AnswerGenerator answerGenerator;
	private final QuestionGenerator questionGenerator;
	private final UserGenerator userGenerator;

	public DeleteHistoryRepositoryTest(
		DeleteHistoryRepository deleteHistoryRepository,
		AnswerGenerator answerGenerator,
		QuestionGenerator questionGenerator,
		UserGenerator userGenerator
	) {
		this.deleteHistoryRepository = deleteHistoryRepository;
		this.answerGenerator = answerGenerator;
		this.questionGenerator = questionGenerator;
		this.userGenerator = userGenerator;
	}

	@Test
	@DisplayName("질문 삭제 이력 저장 테스트")
	void saveQuestionTest() {
		// given
		User user = userGenerator.savedUser();
		Question question = questionGenerator.savedQuestion(user);
		DeleteHistory deleteHistory =
			new DeleteHistory(ContentType.QUESTION, question.getId(), user, LocalDateTime.now());

		// when
		DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

		// then
		assertThat(actual).isNotNull();
	}

	@Test
	@DisplayName("답변 삭제 이력 저장 테스트")
	void saveAnswerTest() {
		// given
		User questionWriter = userGenerator.savedUser();
		Question question = questionGenerator.savedQuestion(questionWriter);
		User answerWriter = userGenerator.savedUser(UserGenerator.answerWriter());
		Answer answer = answerGenerator.savedAnswer(answerWriter, question, "answer contents");

		DeleteHistory deleteHistory =
			new DeleteHistory(ContentType.ANSWER, answer.getId(), answerWriter, LocalDateTime.now());

		// when
		DeleteHistory actual = deleteHistoryRepository.save(deleteHistory);

		// then
		assertThat(actual).isNotNull();
	}

	@Test
	@DisplayName("삭제 이력 저장 후 조회 테스트")
	void findByIdTest() {
		// given
		User questionWriter = userGenerator.savedUser();
		Question question = questionGenerator.savedQuestion(questionWriter);
		DeleteHistory questionDeleteHistory =
			new DeleteHistory(ContentType.QUESTION, question.getId(), questionWriter, LocalDateTime.now());
		DeleteHistory actualQuestionDeleteHistory = deleteHistoryRepository.save(questionDeleteHistory);

		User answerWriter = userGenerator.savedUser(UserGenerator.answerWriter());
		Answer answer = answerGenerator.savedAnswer(answerWriter, question, "answer contents");
		DeleteHistory answerDeleteHistory =
			new DeleteHistory(ContentType.ANSWER, answer.getId(), answerWriter, LocalDateTime.now());
		DeleteHistory actualAnswerDeleteHistory = deleteHistoryRepository.save(answerDeleteHistory);

		// when
		List<DeleteHistory> deleteHistories = deleteHistoryRepository.findAll();

		// then
		assertThat(deleteHistories).hasSize(2)
				.containsExactly(actualQuestionDeleteHistory, actualAnswerDeleteHistory);
	}
}