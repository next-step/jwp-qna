package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
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

	@Test
	@DisplayName("작성자가 일치하지 않은경우 answer제거 실패")
	void test_작성자불일치_답변제거() {
		Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "test");
		Assertions.assertThatThrownBy(() -> {
			answer.delete(UserTest.SANJIGI);
		}).isInstanceOf(CannotDeleteException.class)
			.hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
	}

	@Test
	@DisplayName("작성자 일치시 답변제거")
	void test_답변제거_작성자일치() throws CannotDeleteException {
		Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "test");
		DeleteHistory deleteHistory = answer.delete(UserTest.JAVAJIGI);

		assertThat(deleteHistory.getDeletedBy()).isEqualTo(UserTest.JAVAJIGI);
		assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER);
		assertThat(deleteHistory.getContentId()).isEqualTo(answer.getId());
		assertThat(deleteHistory.getDeletedBy()).isEqualTo(answer.getWriter());
		assertThat(deleteHistory.getCreateDate()).isNotNull();

	}
}
