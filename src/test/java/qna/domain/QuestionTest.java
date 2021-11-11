package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import qna.CannotDeleteException;
import qna.fixture.AnswerFixture;
import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

@DisplayName("질문")
public class QuestionTest {
	private static Stream<Arguments> ofFailArguments() {
		return Stream.of(
			Arguments.of(
				UserFixture.SEMISTONE222(),
				null,
				IllegalArgumentException.class
			),
			Arguments.of(
				UserFixture.Y2O2U2N(),
				"",
				IllegalArgumentException.class
			)
		);
	}

	public static Stream<Arguments> isOwnerArguments() {
		return Stream.of(
			Arguments.of(
				QuestionFixture.Q1(UserFixture.Y2O2U2N()),
				UserFixture.Y2O2U2N(),
				true
			),
			Arguments.of(
				QuestionFixture.Q1(UserFixture.Y2O2U2N()),
				UserFixture.SEMISTONE222(),
				false
			)
		);
	}

	public static Stream<Arguments> deleteArguments() {
		return Stream.of(
			// 답변이 없는 경우
			Arguments.of(
				QuestionFixture.Q1(UserFixture.Y2O2U2N()),
				UserFixture.Y2O2U2N()
			),
			// 답변이 있지만 모든 답변을 질문자가 작성한 경우
			Arguments.of(
				QuestionFixture.SELF_ANSWERED_Q(UserFixture.Y2O2U2N()),
				UserFixture.Y2O2U2N()
			)
		);
	}

	@DisplayName("질문을 생성할 수 있다.")
	@Test
	void of() {
		// given
		User writer = UserFixture.SEMISTONE222();
		String title = "title";
		String contents = "contents";

		// when
		Question question = Question.of(writer, title, contents);

		// then
		assertAll(
			() -> assertThat(question).isNotNull(),
			() -> assertThat(question.getContents()).isEqualTo(QuestionContents.of(contents)),
			() -> assertThat(question.isDeleted()).isFalse(),
			() -> assertThat(question.getTitle()).isEqualTo(QuestionTitle.of(title)),
			() -> assertThat(question.getWriter()).isEqualTo(writer)
		);
	}

	@DisplayName("질문을 생성할 수 없다.")
	@ParameterizedTest
	@MethodSource(value = "ofFailArguments")
	void of_fail(User writer, String title, Class<?> expectedExceptionType) {
		// given & when & then
		assertThatThrownBy(() -> Question.of(writer, title, "contents"))
			.isInstanceOf(expectedExceptionType);
	}

	@DisplayName("질문에 답변을 등록할 수 있다.")
	@Test
	void addAnswer() {
		// given
		Question question = QuestionFixture.Q1(UserFixture.Y2O2U2N());
		Answer answer = AnswerFixture.A1(UserFixture.SEMISTONE222());

		// when
		question.addAnswer(answer);

		// then
		assertAll(
			() -> assertThat(question.getAnswers()).contains(answer),
			() -> assertThat(answer.getQuestion()).isEqualTo(question),
			() -> assertThat(question.getAnswers()).hasSize(1)
		);
	}

	@DisplayName("질문에 빈 답변을 등록할 수 없다.")
	@Test
	void addAnswer_fail_on_empty_answer() {
		// given
		Question question = QuestionFixture.Q1(UserFixture.Y2O2U2N());

		// when & then
		assertThatThrownBy(() -> question.addAnswer(null))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("질문에 이미 등록된 답변을 등록할 수 없다.")
	@Test
	void addAnswer_fail_on_already_registered_answer() {
		// given
		Question question = QuestionFixture.Q1(UserFixture.Y2O2U2N());
		Answer answer = AnswerFixture.A1(UserFixture.SEMISTONE222());
		question.addAnswer(answer);

		// when & then
		assertThatThrownBy(() -> question.addAnswer(answer))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("질문을 삭제할 수 있다.")
	@ParameterizedTest
	@MethodSource(value = "deleteArguments")
	void delete(Question question, User deleter) {
		// given & when
		List<DeleteHistory> deleteHistories = question.delete(deleter);

		// then
		assertAll(
			() -> assertThat(question.isDeleted()).isTrue(),
			() -> question.getAnswers().forEach((answer) -> assertThat(answer.isDeleted()).isTrue()),
			() -> assertThat(deleteHistories)
				.extracting(deleteHistory -> deleteHistory.getContentType() == ContentType.QUESTION)
				.isNotNull()
		);

	}

	@DisplayName("본인이 작성하지 않은 질문은 삭제할 수 없다.")
	@Test
	void delete_fail_on_not_question_owner() {
		// given
		Question question = QuestionFixture.Q1(UserFixture.Y2O2U2N());
		User deleter = UserFixture.SEMISTONE222();

		// when
		assertThatThrownBy(() -> question.delete(deleter))
			.isInstanceOf(CannotDeleteException.class);
	}

	@DisplayName("본인이 등록하지 않은 답변이 있을 경우, 질문을 삭제할 수 없다.")
	@Test
	void delete_fail_on_not_answer_owner() {
		// given
		User questionWriter = UserFixture.Y2O2U2N();
		Question question = QuestionFixture.Q1(questionWriter);
		List<Answer> answers = Arrays.asList(
			AnswerFixture.A1(questionWriter),
			AnswerFixture.A2(questionWriter),
			AnswerFixture.A3(UserFixture.JUN222())
		);
		answers.forEach(question::addAnswer);

		// when
		assertThatThrownBy(() -> question.delete(questionWriter))
			.isInstanceOf(CannotDeleteException.class);
	}

	@DisplayName("사용자가 질문의 주인인지 알 수 있다.")
	@ParameterizedTest
	@MethodSource(value = "isOwnerArguments")
	void isOwner(Question question, User user, boolean expected) {
		// given & when
		boolean actual = question.isOwner(user);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
