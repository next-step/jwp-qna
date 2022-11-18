package qna.domain.question;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.generator.UserGenerator.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
import qna.domain.answer.Answer;
import qna.domain.deletehistory.DeleteHistory;
import qna.domain.generator.AnswerGenerator;
import qna.domain.generator.QuestionGenerator;
import qna.domain.generator.UserGenerator;
import qna.domain.user.User;
import qna.domain.user.UserTest;

public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	@Test
	@DisplayName("새로운 답변 등록 시 답변 목록 추가 테스트")
	void addAnswer() {
		// given
		Question question = new Question("title", "contents");
		question.writeBy(UserGenerator.questionWriter());
		Answer answer = new Answer(UserGenerator.answerWriter(), question, "contents");

		// when
		question.addAnswer(answer);

		// then
		assertThat(question.getAnswers().isEmpty()).isFalse();
	}

	@Test
	@DisplayName("질문 작성자와 로그인 사용자가 다르면 예외 발생")
	void deleteQuestionByOtherUser () {
		// given
		final User questionWriter = questionWriter();
		final Question question = QuestionGenerator.question(questionWriter);

		final User answerWriter = answerWriter();
		AnswerGenerator.answer(questionWriter, question, "answer_contents1");
		AnswerGenerator.answer(questionWriter, question, "answer_contents2");
		AnswerGenerator.answer(answerWriter, question, "answer_contents3");

		final User loginUser = anotherUser();

		// when & then
		assertThatExceptionOfType(CannotDeleteException.class)
			.isThrownBy(() -> question.delete(loginUser));
	}

	@Test
	@DisplayName("질문 작성자와 답변 작성자가 다른 경우 예외 발생")
	void deleteQuestionByAnotherAnswerWriter() {
		// given
		final User questionWriter = questionWriter();
		final Question question = QuestionGenerator.question(questionWriter);

		final User answerWriter = answerWriter();
		AnswerGenerator.answer(questionWriter, question, "answer_contents1");
		AnswerGenerator.answer(questionWriter, question, "answer_contents2");
		AnswerGenerator.answer(answerWriter, question, "answer_contents3");

		// when & then
		assertThatExceptionOfType(CannotDeleteException.class)
			.isThrownBy(() -> question.delete(questionWriter));
	}

	@Test
	@DisplayName("질문 작성자와 답변 작성자가 같은 경우 삭제 처리 후 삭제 이력 반환")
	void deleteTest() {
		// Given
		final User questionWriter = questionWriter();
		final Question question = QuestionGenerator.question(questionWriter);

		final User answerWriter = answerWriter();
		AnswerGenerator.answer(questionWriter, question, "answer_contents1");
		AnswerGenerator.answer(questionWriter, question, "answer_contents2");
		AnswerGenerator.answer(questionWriter, question, "answer_contents3");

		// When
		final List<DeleteHistory> deleteHistories = question.delete(questionWriter);

		// Then
		assertThat(deleteHistories).isNotNull();
	}
}
