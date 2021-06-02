package qna.domain.question;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.domain.answer.AnswerTest;
import qna.domain.history.DeleteHistorys;
import qna.domain.user.UserTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class AnswersInQuestionTest {


	@Test
	@DisplayName("각기 다른 답변 소유자를 가진 AnswerInQuestion 테스트")
	void checkOwnerTest() {
		// given
		AnswersInQuestion answers = new AnswersInQuestion();
		answers.add(AnswerTest.A1);
		answers.add(AnswerTest.A2);

		// when
		assertThatThrownBy(() -> answers.checkOwner(UserTest.JAVAJIGI))
			.isInstanceOf(CannotDeleteException.class)
			.hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다."); // then
	}

	@Test
	@DisplayName("답변 전체 삭제 테스트")
	void deleteAllTest() {
		// given
		AnswersInQuestion answers = new AnswersInQuestion();
		answers.add(AnswerTest.A1);
		answers.add(AnswerTest.A2);

		// when
		DeleteHistorys deleteHistorys = answers.deleteAll();

		// then
		assertAll(() -> {
			assertThat(deleteHistorys).isNotNull();
			assertThat(deleteHistorys.values()).hasSize(2);
		});
	}
}
