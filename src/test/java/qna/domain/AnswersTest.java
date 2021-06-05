package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

class AnswersTest {

	private Question question;
	private User loginUser;

	@BeforeEach
	void setup() {
		this.loginUser = new User(99L, "testUser", "testPassword", "testName", "testEmail");
		this.question = new Question("testQuestion", "testContents").writeBy(this.loginUser);
	}

	@Test
	@DisplayName("답변 목록 삭제 테스트")
	void test_deleteAnswers() throws CannotDeleteException {
		Answers answers = new Answers();
		Answer answer1 = new Answer(this.loginUser, this.question, "answer1");
		Answer answer2 = new Answer(this.loginUser, this.question, "answer2");
		answers.addAnswer(answer1);
		answers.addAnswer(answer2);

		List<DeleteHistory> deleteHistories = answers.deleteAnswers(this.loginUser);
		assertThat(deleteHistories.contains(answer1)).isFalse();
		assertThat(deleteHistories.contains(answer2)).isFalse();
		assertThat(answer1.isDeleted()).isTrue();
		assertThat(answer2.isDeleted()).isTrue();
	}

	@Test
	@DisplayName("답변중 작성자가 다르면 삭제실패")
	void test_deleteAnswer_diffWriter() {
		Answers answers = new Answers();
		Answer answer1 = new Answer(this.loginUser, this.question, "answer1");
		Answer answer2 = new Answer(UserTest.JAVAJIGI, this.question, "answer2");
		answers.addAnswer(answer1);
		answers.addAnswer(answer2);

		assertThatThrownBy(() -> {
			answers.deleteAnswers(this.loginUser);
		}).isInstanceOf(CannotDeleteException.class)
			.hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
	}
}