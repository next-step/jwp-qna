package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	private User loginUser;
	private Question question;

	@BeforeEach
	void setup() {
		this.loginUser = new User("testUser", "testPassword", "testName", "testEmail");
		this.question = new Question("testQuestion", "testContents").writeBy(this.loginUser);
	}

	@Test
	@DisplayName("질문 작성자가 아닌경우 오류발생")
	void test_질문삭제_권한없음() {
		Assertions.assertThatThrownBy(() -> {
			question.delete(UserTest.JAVAJIGI);
		}).isInstanceOf(CannotDeleteException.class)
			.hasMessageContaining("질문을 삭제할 권한이 없습니다.");
	}
}
