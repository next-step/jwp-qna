package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

class AnswersTest {

	private Question question;
	private Answers answers;
	private User user;

	@BeforeEach
	void setUp() {
		this.question = new Question("title1", "contents1");

		this.user = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		Answer answer1 = new Answer(user, question,"Answers Contents1");
		Answer answer2 = new Answer(user, question,"Answers Contents1");

		this.answers = new Answers(Arrays.asList(answer1, answer2));
	}

	@DisplayName("답변 중 로그인한 사람과 다른 사람의 답변이 달려있는 경우, 에러를 발생한다.")
	@Test
	void validateDeleteAnswer() {
		User loginUser = new User(2L, "test", "password", "name", "test@slipp.net");

		assertThatThrownBy(() -> this.answers.deleteAnswer(loginUser))
			.isInstanceOf(CannotDeleteException.class);
	}
}
