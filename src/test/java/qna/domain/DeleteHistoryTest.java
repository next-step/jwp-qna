package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.fixture.AnswerFixture;
import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

@DisplayName("삭제 기록")
class DeleteHistoryTest {

	@DisplayName("질문 삭제 기록을 생성할 수 있다.")
	@Test
	void ofQuestion() {
		// given
		Question question = QuestionFixture.Q1(1L, UserFixture.Y2O2U2N(2L));
		User deleter = UserFixture.SEMISTONE222(3L);

		// when
		DeleteHistory deleteHistory = DeleteHistory.ofQuestion(deleter, question);

		// then
		assertAll(
			() -> assertThat(deleteHistory).isNotNull(),
			() -> assertThat(deleteHistory.getContentId()).isEqualTo(question.getId()),
			() -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION),
			() -> assertThat(deleteHistory.getDeleter()).isEqualTo(deleter)
		);
	}

	@DisplayName("답변 삭제 기록을 생성할 수 있다.")
	@Test
	void ofAnswer() {
		// given
		Question question = QuestionFixture.Q1(1L, UserFixture.Y2O2U2N(2L));
		Answer answer = AnswerFixture.A1(3L, UserFixture.JUN222(4L), question);
		User deleter = UserFixture.SEMISTONE222(5L);

		// when
		DeleteHistory deleteHistory = DeleteHistory.ofAnswer(deleter, answer);

		// then
		assertAll(
			() -> assertThat(deleteHistory).isNotNull(),
			() -> assertThat(deleteHistory.getContentId()).isEqualTo(answer.getId()),
			() -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
			() -> assertThat(deleteHistory.getDeleter()).isEqualTo(deleter)
		);
	}
}
