package qna.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.QuestionNotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
	public static final Answer A1 = new Answer(JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
	public static final String CONTENTS = "답변1";

	@Test
	@DisplayName("Answer 생성시 필수여부검사")
	void given_A1_When_initAnswer_Then_require_WriterIdAndQuestionId_isTrue() {

		// then
		assertThat(A1.getQuestion().equals(Q1)).isTrue();
	}

	@Test
	@DisplayName("A1가 삭제여부를 true 변경했을 때 삭제되었는지 확인")
	void given_A1_When_delete_Then_isTrue() {

		// when
		A1.setDeleted(true);

		// then
		Assertions.assertThat(A1.isDeleted()).isTrue();
	}

	@Test
	@DisplayName("유저가 없는 Answer 생성할 때 예외")
	void given_Answer_When_userIsNull_Then_UnAuthorizedException() {

		// then
		Assertions.assertThatThrownBy(() -> {
			Answer answer = new Answer(null, Q1, CONTENTS);
		}).isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("Qna가 null이 주어졌을 때 Answer 생서시 예외,")
	void given_Answer_When_Question_Then_NotFoundException() {

		// then
		Assertions.assertThatThrownBy(() -> {
			Answer answer = new Answer(JAVAJIGI, null, CONTENTS);
		}).isInstanceOf(QuestionNotFoundException.class);
	}
}
