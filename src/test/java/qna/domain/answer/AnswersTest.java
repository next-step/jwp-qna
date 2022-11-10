package qna.domain.answer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
import qna.domain.generator.AnswerGenerator;
import qna.domain.generator.QuestionGenerator;
import qna.domain.generator.UserGenerator;
import qna.domain.question.Question;
import qna.domain.user.User;

@DisplayName("답변 컬렉션 테스트")
class AnswersTest {

	@Test
	@DisplayName("답변 컬렉션 생성")
	void createAnswersTest() {
		// given
		User user = UserGenerator.questionWriter();
		Question question = QuestionGenerator.question(user);

		AnswerGenerator.answer(user, question, "answer1");
		AnswerGenerator.answer(user, question, "answer2");
		AnswerGenerator.answer(user, question, "answer3");

		// when
		Answers answers = question.getAnswers();

		assertThat(answers).isNotNull();
	}

	@Test
	@DisplayName("질문에 연결된 모든 답변을 삭제")
	void allMatchLoginUserTest() {
		// given
		User user = UserGenerator.questionWriter();
		Question question = QuestionGenerator.question(user);

		AnswerGenerator.answer(user, question, "answer1");
		AnswerGenerator.answer(user, question, "answer2");
		AnswerGenerator.answer(user, question, "answer3");

		// when
		Answers answers = question.getAnswers();

		// then
		assertThat(answers.deleteAll(user)).hasSize(3);
	}

	@Test
	@DisplayName("하나의 답변이라도 로그인 사용자와 다르면 예외 발생")
	void allMatchLoginUserTest2() {
		// given
		User user = UserGenerator.questionWriter();
		Question question = QuestionGenerator.question(user);

		AnswerGenerator.answer(user, question, "answer1");
		AnswerGenerator.answer(UserGenerator.anotherUser(), question, "answer2");
		AnswerGenerator.answer(user, question, "answer3");

		// when
		Answers answers = question.getAnswers();

		// then
		assertThatThrownBy(() -> answers.deleteAll(user))
			.isInstanceOf(CannotDeleteException.class);
	}

}