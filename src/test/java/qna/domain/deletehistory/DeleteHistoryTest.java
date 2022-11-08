package qna.domain.deletehistory;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.domain.generator.AnswerGenerator;
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
		DeleteHistory deleteHistory = DeleteHistory.questionDeleteHistory(question);
		assertThat(deleteHistory).isInstanceOf(DeleteHistory.class);
	}

	@Test
	@DisplayName("답변이 없는 질문 삭제 시, 삭제 이력 생성")
	void createDeleteHistoryWhenNoAnswer() {
		// Given
		final User questionWriter = UserGenerator.questionWriter();
		final Question question = QuestionGenerator.question(questionWriter);

		// When
		List<DeleteHistory> actual = DeleteHistory.questionDeleteHistories(question);

		// Then
		assertThat(actual).hasSize(1);
	}

	@Test
	@DisplayName("답변이 포함된 질문 삭제 시, 삭제 이력 생성")
	void createDeleteHistoriesWhenHasAnswer() {
		// Given
		final User questionWriter = UserGenerator.questionWriter();
		final Question question = QuestionGenerator.question(questionWriter);
		AnswerGenerator.answer(questionWriter, question, "answer1");
		AnswerGenerator.answer(questionWriter, question, "answer2");
		AnswerGenerator.answer(questionWriter, question, "answer3");

		// When
		List<DeleteHistory> actual = DeleteHistory.questionDeleteHistories(question);

		// Then
		assertThat(actual).hasSize(4);
	}
}