package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import qna.fixture.AnswerFixture;
import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

@DisplayName("질문")
public class QuestionTest {
	private static Stream<Arguments> ofFailArguments() {
		return Stream.of(
			Arguments.of(
				UserFixture.SEMISTONE222(1),
				null,
				IllegalArgumentException.class
			),
			Arguments.of(
				UserFixture.Y2O2U2N(3),
				"",
				IllegalArgumentException.class
			)
		);
	}

	public static Stream<Arguments> isOwnerArguments() {
		return Stream.of(
			Arguments.of(
				QuestionFixture.Q1(1L, UserFixture.Y2O2U2N(3)),
				UserFixture.Y2O2U2N(3),
				true
			),
			Arguments.of(
				QuestionFixture.Q1(1L, UserFixture.Y2O2U2N(3)),
				UserFixture.SEMISTONE222(1),
				false
			)
		);
	}

	@DisplayName("질문을 생성할 수 있다.")
	@Test
	void of() {
		// given
		User writer = UserFixture.SEMISTONE222(1);
		String title = "title";
		String contents = "contents";

		// when
		Question question = Question.of(writer, title, contents);

		// then
		assertAll(
			() -> assertThat(question).isNotNull(),
			() -> assertThat(question.getContents()).isEqualTo(contents),
			() -> assertThat(question.isDeleted()).isFalse(),
			() -> assertThat(question.getTitle()).isEqualTo(title),
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
		Question question = QuestionFixture.Q1(1L, UserFixture.Y2O2U2N(2L));
		Answer answer = AnswerFixture.A1(3L, UserFixture.SEMISTONE222(4L));

		// when
		question.addAnswer(answer);

		// then
		assertAll(
			() -> assertThat(question.getAnswers()).contains(answer),
			() -> assertThat(answer.getQuestion()).isEqualTo(question)
		);
	}

	@DisplayName("질문에 빈 답변을 등록할 수 없다.")
	@Test
	void addAnswer_fail_on_empty_answer() {
		// given
		Question question = QuestionFixture.Q1(1L, UserFixture.Y2O2U2N(2L));

		// when & then
		assertThatThrownBy(() -> question.addAnswer(null))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("질문에 이미 등록된 답변을 등록할 수 없다.")
	@Test
	void addAnswer_fail_on_already_registered_answer() {
		// given
		Question question = QuestionFixture.Q1(1L, UserFixture.Y2O2U2N(2L));
		Answer answer = AnswerFixture.A1(3L, UserFixture.SEMISTONE222(4L));
		question.addAnswer(answer);

		// when & then
		assertThatThrownBy(() -> question.addAnswer(answer))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("질문을 삭제할 수 있다.")
	@Test
	void delete() {
		// given
		Question question = QuestionFixture.Q1(1L, UserFixture.Y2O2U2N());

		// when
		question.delete();

		// then
		assertThat(question.isDeleted()).isTrue();
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
