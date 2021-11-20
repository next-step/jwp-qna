package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	@Test
	@DisplayName("ID가 같다면 객체도 같아야 한다")
	void equals_test() {
		// given
		Question q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
		Question q2 = new Question(1L, "title2", "contents2").writeBy(UserTest.JAVAJIGI);

		// when, then
		assertThat(q1).isEqualTo(q2)
			.hasSameHashCodeAs(q2);
	}

	@Test
	@DisplayName("주어진 사용자와 질문자가 다르면 삭제가 불가능하다")
	void delete_test_diff_writer() {
		// given
		Question question = new Question(1L, "title1", "contents1")
			.writeBy(UserTest.JAVAJIGI);
		User loginUser = UserTest.SANJIGI;

		// when, then
		assertThatThrownBy(() -> question.delete(loginUser)
		).isInstanceOf(CannotDeleteException.class);

	}

	@Test
	@DisplayName("질문자와 다른 답변이 하나라도 있으면 삭제가 불가능하다")
	void delete_test_diff_answer_writer() {
		// given
		User loginUser = UserTest.SANJIGI;
		Question question = new Question(1L, "title1", "contents1")
			.writeBy(loginUser);
		Answer a1 = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
		Answer a2 = new Answer(2L, loginUser, question, "Answers Contents1");
		question.addAnswers(Arrays.asList(a1, a2));

		// when, then
		assertThatThrownBy(() -> question.delete(loginUser))
			.isInstanceOf(CannotDeleteException.class);
	}

	@Test
	@DisplayName("정상적으로 질문이 삭제되어야 한다")
	void delete_test() throws CannotDeleteException {
		// given
		User loginUser = UserTest.SANJIGI;
		Question question = new Question(1L, "title1", "contents1")
			.writeBy(loginUser);
		Answer a1 = new Answer(1L, loginUser, question, "Answers Contents1");
		Answer a2 = new Answer(2L, loginUser, question, "Answers Contents1");
		question.addAnswers(Arrays.asList(a1, a2));

		DeleteHistory h1 = new DeleteHistory(ContentType.QUESTION, 1L, loginUser, LocalDateTime.now());
		DeleteHistory h2 = new DeleteHistory(ContentType.ANSWER, 1L, loginUser, LocalDateTime.now());
		DeleteHistory h3 = new DeleteHistory(ContentType.ANSWER, 2L, loginUser, LocalDateTime.now());
		List<DeleteHistory> expected = new ArrayList<>(Arrays.asList(h1, h2, h3));

		// when
		List<DeleteHistory> result = question.delete(loginUser);

		// then
		assertAll(
			() -> assertThat(result).isEqualTo(expected),
			() -> assertThat(question.isDeleted()).isTrue(),
			() -> assertThat(a1.isDeleted()).isTrue(),
			() -> assertThat(a2.isDeleted()).isTrue()
		);
	}
}
