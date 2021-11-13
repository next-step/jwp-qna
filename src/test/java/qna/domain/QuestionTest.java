package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.ErrorMessage;
import qna.UnAuthorizedException;

public class QuestionTest {
	public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);

	@Test
	@DisplayName("유저정보가 null인 Question 생성시 예외")
	void given_userNull_then_UnAuthorizedException() {
		Assertions.assertThatThrownBy(() -> {
				new Question(3L, "title3", "content3").writeBy(null);
			}).isInstanceOf(UnAuthorizedException.class)
			.hasMessage(ErrorMessage.USER_IS_NOT_NULL);
	}

	@Test
	@DisplayName("Q1 삭제여부를 true 변경했을 때 true 확인")
	void given_Q1_when_Change_Delete_To_true_then_isTrue() {

		// when
		Q1.setDeleted(true);

		// then
		Assertions.assertThat(Q1.isDeleted()).isTrue();
	}

}
