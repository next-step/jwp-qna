package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

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
		assertThatThrownBy(() -> {
			question.delete(UserTest.JAVAJIGI);
		}).isInstanceOf(CannotDeleteException.class)
			.hasMessageContaining("질문을 삭제할 권한이 없습니다.");
	}

	@Test
	@DisplayName("질문 작성자 일치하는 경우 삭제")
	void test_질문삭제() throws CannotDeleteException {
		List<DeleteHistory> deleteHistories = this.question.delete(this.loginUser);
		assertThat(deleteHistories.size()).isEqualTo(1);
		assertThat(this.question.isDeleted()).isTrue();
	}
}
