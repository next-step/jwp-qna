package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {

	private Question question;

	private User javajigi;
	private User sanjigi;

	@BeforeEach
	public void setUp() throws Exception {
		javajigi = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		sanjigi = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		question = new Question(1L, "title1", "contents1").writeBy(javajigi);
	}

	@Test
	@DisplayName("delete 성공 테스트")
	void delete_성공() throws Exception {
		final DeleteHistories deleteHistories = question.delete(javajigi);
		assertThat(new DeleteHistories(DeleteHistory.ofQuestion(question.getId(), javajigi))).isEqualTo(
			deleteHistories);
	}

	@Test
	@DisplayName("다른 사람의 글을 delete하여 실패하는 테스트")
	void delete_다른사람의_글_실패() {
		assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() -> {
			question.delete(sanjigi);
		});
	}
}
