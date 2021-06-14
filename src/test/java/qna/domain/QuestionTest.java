package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {

	private Question question;
	private User user;

	@BeforeEach
	void setUp() {
		this.user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");

		this.question = new Question("title1", "contents1");
		this.question.writeBy(user);
	}

	@Test
	@DisplayName("질문의 주인이 아닌 경우, 삭제할 수 없다.")
	void validateDeleteQuestion() {
		User loginUser = new User(2L, "test", "password", "name", "test@slipp.net");

		assertThatThrownBy(() -> this.question.deleteQuestion(loginUser))
			.isInstanceOf(CannotDeleteException.class);
	}

	@Test
	@DisplayName("질문이 삭제 된다.")
	void deleteQuestion() throws CannotDeleteException {
		this.question.deleteQuestion(user);

		assertThat(this.question.isDeleted()).isTrue();
	}
}
