package qna.domain.question;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.domain.answer.Answer;
import qna.domain.generator.UserGenerator;
import qna.domain.user.UserTest;

public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	@Test
	@DisplayName("새로운 답변 등록 시 답변 목록 추가 테스트")
	void addAnswer() {
		// Given
		Question question = new Question("title", "contents");
		question.writeBy(UserGenerator.questionWriter());
		Answer answer = new Answer(UserGenerator.answerWriter(), question, "contents");

		// When
		question.addAnswer(answer);

		// Then
		assertThat(question.getAnswers()).containsExactly(answer);
	}

	@Test
	@DisplayName("동일 답변 중복 등록 시 목록 추가 제외 테스트")
	void addDuplicateAnswerTest() {
		// Given
		Question question = new Question("title", "contents");
		question.writeBy(UserGenerator.questionWriter());
		Answer answer = new Answer(UserGenerator.answerWriter(), question, "contents");

		// When
		question.addAnswer(answer);
		question.addAnswer(answer);

		// Then
		assertThat(question.getAnswers()).containsExactly(answer);
	}
}
