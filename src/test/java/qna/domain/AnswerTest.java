package qna.domain;

import static org.assertj.core.api.Assertions.*;

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
		assertThatThrownBy(() -> {
			new Answer(null, QuestionTest.Q1, "test");
		}).isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("answer 생성시 Question 이 null인 경우 NotFoundException 확인")
	void test_AnswerQuestionNull() {
		assertThatThrownBy(() -> {
			new Answer(UserTest.JAVAJIGI, null, "test");
		}).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("answer writer가 맞는지 테스트")
	void test_isOwner() {
		Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "test");
		assertThat(answer.isOwner(null)).isFalse();
		assertThat(answer.isOwner(UserTest.SANJIGI)).isFalse();
		assertThat(answer.isOwner(UserTest.JAVAJIGI)).isTrue();
	}
}
