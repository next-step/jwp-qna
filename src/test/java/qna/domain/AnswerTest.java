package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	@Test
	@DisplayName("answer 생성시 User가 null인경우 UnAuthorizedException 확인")
	void test_Answer생성오류확인() {
		Assertions.assertThatThrownBy(() -> {
			new Answer(null, QuestionTest.Q1, "test");
		}).isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("answer 생성시 Question 이 null인 경우 NotFoundException 확인")
	void test_AnswerQuestionNull() {
		Assertions.assertThatThrownBy(() -> {
			new Answer(UserTest.JAVAJIGI, null, "test");
		}).isInstanceOf(NotFoundException.class);
	}
}
