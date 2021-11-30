package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswersTest {
	private Question question;
	private Answers answers;
	private Answer answer;

	private User javajigi;
	private User sanjigi;

	@BeforeEach
	public void setUp() throws Exception {
		javajigi = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
		sanjigi = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
		question = new Question(1L, "title1", "contents1").writeBy(javajigi);
		answer = new Answer(1L, javajigi, question, "Answers Contents1");
		answers = new Answers(answer);
	}

	@Test
	@DisplayName("delete 성공 테스트")
	void delete_성공() throws Exception {
		final DeleteHistories deleteHistories = answers.delete(javajigi);
		assertThat(deleteHistories.contains(DeleteHistory.ofAnswer(answer.getId(), javajigi))).isTrue();
	}

	@Test
	@DisplayName("다른 사람의 댓글을 delete하여 실패하는 테스트")
	void delete_다른사람의_댓글_실패() {
		answers.add(new Answer(2L, sanjigi, question, "Answers Contents1"));
		assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() -> {
			answers.delete(javajigi);
		});
	}
}
