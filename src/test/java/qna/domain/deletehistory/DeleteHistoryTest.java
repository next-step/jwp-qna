package qna.domain.deletehistory;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.domain.generator.QuestionGenerator;
import qna.domain.generator.UserGenerator;
import qna.domain.question.Question;
import qna.domain.user.User;

@DisplayName("삭제 이력 테스트")
class DeleteHistoryTest {

	@Test
	@DisplayName("삭제 이력 생성")
	void createDeleteHistory() {
		User user = UserGenerator.questionWriter();
		Question question = QuestionGenerator.question(user);
		DeleteHistory deleteHistory = DeleteHistory.ofQuestion(question);
		assertThat(deleteHistory).isInstanceOf(DeleteHistory.class);
	}

}