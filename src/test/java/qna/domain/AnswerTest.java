package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.fixture.AnswerFixture;
import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

@DisplayName("답변")
public class AnswerTest {
	private static Stream<Arguments> ofFailArguments() {
		return Stream.of(
			Arguments.of(
				null,
				QuestionFixture.Q1(2, UserFixture.SEMISTONE222(1)),
				"contents",
				UnAuthorizedException.class
			),
			Arguments.of(
				UserFixture.Y2O2U2N(3),
				null,
				"contents",
				NotFoundException.class
			)
		);
	}

	public static Stream<Arguments> isOwnerArguments() {
		return Stream.of(
			Arguments.of(
				AnswerFixture.A1(1L, UserFixture.Y2O2U2N(3), QuestionFixture.Q1(2, UserFixture.SEMISTONE222(1))),
				UserFixture.Y2O2U2N(3),
				true
			),
			Arguments.of(
				AnswerFixture.A1(1L, UserFixture.Y2O2U2N(3), QuestionFixture.Q1(2, UserFixture.SEMISTONE222(1))),
				UserFixture.SEMISTONE222(1),
				false
			)
		);
	}

	@DisplayName("답변을 생성할 수 있다.")
	@Test
	void of() {
		// given
		User questionWriter = UserFixture.SEMISTONE222(1);
		Question question = QuestionFixture.Q1(2, questionWriter);
		User answerWriter = UserFixture.Y2O2U2N(3);
		String contents = "contents";

		// when
		Answer answer = Answer.of(answerWriter, question, contents);

		// then
		assertAll(
			() -> assertThat(answer).isNotNull(),
			() -> assertThat(answer.getContents()).isEqualTo(contents),
			() -> assertThat(answer.isDeleted()).isFalse(),
			() -> assertThat(answer.getQuestionId()).isEqualTo(question.getId()),
			() -> assertThat(answer.getWriter()).isEqualTo(answerWriter)
		);
	}

	@DisplayName("답변을 생성할 수 없다.")
	@ParameterizedTest
	@MethodSource(value = "ofFailArguments")
	void of_fail(User writer, Question question, String contents, Class<?> expectedExceptionType) {
		// given & when & then
		assertThatThrownBy(() -> Answer.of(writer, question, contents))
			.isInstanceOf(expectedExceptionType);
	}

	@DisplayName("답변을 삭제할 수 있다.")
	@Test
	void delete() {
		// given
		Answer answer = AnswerFixture.A1(1L, UserFixture.Y2O2U2N(), QuestionFixture.Q1(UserFixture.SEMISTONE222()));

		// when
		answer.delete();

		// then
		assertThat(answer.isDeleted()).isTrue();
	}

	@DisplayName("사용자가 답변의 주인인지 알 수 있다.")
	@ParameterizedTest
	@MethodSource(value = "isOwnerArguments")
	void isOwner(Answer answer, User user, boolean expected) {
		// given & when
		boolean actual = answer.isOwner(user);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
