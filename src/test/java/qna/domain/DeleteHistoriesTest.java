package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

class DeleteHistoriesTest {

	@Test
	@DisplayName("Question과 Answers 삭제 처리 기록 생성")
	public void addDeleteHistory() throws CannotDeleteException {
		Question question =new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
		question.addAnswer(AnswerTest.A1);
		question.delete(UserTest.JAVAJIGI);

		DeleteHistories deleteHistories = new DeleteHistories();
		deleteHistories.addDeleteHistory(question,UserTest.JAVAJIGI);

		assertThat(deleteHistories.getNumOfDeletedContents()).isEqualTo(2);
	}

	@Test
	@DisplayName("삭제되지 않은 컨텐츠 삭제하려할 경우 예외")
	public void addDeleteHistory_예외() {
		Question question =new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
		question.addAnswer(AnswerTest.A1);

		DeleteHistories deleteHistories = new DeleteHistories();
		Assertions.assertThatThrownBy(()->{
			deleteHistories.addDeleteHistory(question,UserTest.JAVAJIGI);
		}).isInstanceOf(IllegalStateException.class);
	}

}