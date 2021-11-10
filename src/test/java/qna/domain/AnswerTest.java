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
				"contents",
				UnAuthorizedException.class
			)
		);
	}

	public static Stream<Arguments> isOwnerArguments() {
		return Stream.of(
			Arguments.of(
				AnswerFixture.A1(1L, UserFixture.Y2O2U2N(3L)),
				UserFixture.Y2O2U2N(3L),
				true
			),
			Arguments.of(
				AnswerFixture.A1(1L, UserFixture.Y2O2U2N(2L)),
				UserFixture.SEMISTONE222(3L),
				false
			)
		);
	}

	@DisplayName("답변을 생성할 수 있다.")
	@Test
	void of() {
		// given
		User writer = UserFixture.Y2O2U2N(1L);
		String contents = "contents";

		// when
		Answer answer = Answer.of(writer, contents);

		// then
		assertAll(
			() -> assertThat(answer).isNotNull(),
			() -> assertThat(answer.getContents()).isEqualTo(contents),
			() -> assertThat(answer.isDeleted()).isFalse(),
			() -> assertThat(answer.getWriter()).isEqualTo(writer)
		);
	}

	@DisplayName("답변을 생성할 수 없다.")
	@ParameterizedTest
	@MethodSource(value = "ofFailArguments")
	void of_fail(User writer, String contents, Class<?> expectedExceptionType) {
		// given & when & then
		assertThatThrownBy(() -> Answer.of(writer, contents))
			.isInstanceOf(expectedExceptionType);
	}

	@DisplayName("답변에 질문을 등록할 수 있다.")
	@Test
	void setQuestion() {
		// given
		Question question = QuestionFixture.Q1(1L, UserFixture.SEMISTONE222(2L));
		Answer answer = AnswerFixture.A1(3L, UserFixture.Y2O2U2N(4L));

		// when
		answer.setQuestion(question);

		// then
		assertAll(
			() -> assertThat(answer).isNotNull(),
			() -> assertThat(answer.getQuestion()).isEqualTo(question)
		);
	}

	@DisplayName("답변에 질문을 등록할 수 없다.")
	@Test
	void setQuestion_fail() {
		// given
		Answer answer = AnswerFixture.A1(1L, UserFixture.Y2O2U2N(2L));

		// when & then
		assertThatThrownBy(() -> answer.setQuestion(null))
			.isInstanceOf(NotFoundException.class);
	}

	@DisplayName("답변을 삭제할 수 있다.")
	@Test
	void delete() {
		// given
		Answer answer = AnswerFixture.A1(1L, UserFixture.Y2O2U2N());

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
