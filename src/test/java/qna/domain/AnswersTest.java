package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswersTest {

	@Test
	@DisplayName("답변 목록으로 생성할 수 있다")
	void constructorTest() {
		// given
		Answer a1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer a2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
		Answers expected = Answers.of(Arrays.asList(a1, a2));

		// when
		Answers answers = Answers.of(Arrays.asList(a1, a2));

		// then
		assertThat(answers).isEqualTo(expected);
	}

	@Test
	@DisplayName("답변자가 모두 같으면 답변들을 모두 삭제할 수 있다")
	void deleteAllTest() throws CannotDeleteException {
		// given
		Answer a1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer a2 = new Answer(2L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents2");
		Answers answers = Answers.of(Arrays.asList(a1, a2));
		DeleteHistory h1 = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
		DeleteHistory h2 = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.JAVAJIGI, LocalDateTime.now());
		DeleteHistories expected = DeleteHistories.of(Arrays.asList(h1, h2));

		// when
		DeleteHistories result = answers.deleteAll(UserTest.JAVAJIGI);

		// then
		assertThat(result).isEqualTo(expected);
	}

	@Test
	@DisplayName("답변자가 모두 같지 않으면 답변을 삭제할 수 없다")
	void deleteAllExceptionTest() throws CannotDeleteException {
		// given
		Answer a1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer a2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
		Answers answers = Answers.of(Arrays.asList(a1, a2));
		DeleteHistory h1 = new DeleteHistory(ContentType.ANSWER, 1L, UserTest.JAVAJIGI, LocalDateTime.now());
		DeleteHistory h2 = new DeleteHistory(ContentType.ANSWER, 2L, UserTest.SANJIGI, LocalDateTime.now());
		DeleteHistories expected = DeleteHistories.of(Arrays.asList(h1, h2));

		// when
		assertThatThrownBy(() ->
			answers.deleteAll(UserTest.JAVAJIGI)
		).isInstanceOf(CannotDeleteException.class);

	}

	@Test
	@DisplayName("답변들을 추가할 수 있어야 한다")
	void addAllTest() {
		// given
		Answer a1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answer a2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
		Answers expected = Answers.of(Arrays.asList(a1, a2));

		// when
		Answers answers = Answers.of().addAll(Arrays.asList(a1, a2));

		// then
		assertThat(answers).isEqualTo(expected);
	}

	@Test
	@DisplayName("답변을 하나 추가할 수 있어야 한다")
	void addATest() {
		// given
		Answer a1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		Answers expected = Answers.of(Collections.singletonList(a1));

		// when
		Answers answers = Answers.of().add(a1);

		// then
		assertThat(answers).isEqualTo(expected);
	}
}
