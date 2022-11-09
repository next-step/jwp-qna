package qna.domain.answer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
		Answers answers = Answers.of(question);

		assertThat(answers).isInstanceOf(Answers.class);
	}

	@Test
	@DisplayName("모든 답변이 로그인 사용자와 같은지 확인")
	void allMatchLoginUserTest() {
		// given
		User user = UserGenerator.questionWriter();
		Question question = QuestionGenerator.question(user);

		AnswerGenerator.answer(user, question, "answer1");
		AnswerGenerator.answer(user, question, "answer2");
		AnswerGenerator.answer(user, question, "answer3");

		// when
		Answers answers = Answers.of(question);

		// then
		assertThat(answers.allMatchLoginUser(user)).isTrue();
	}

	@Test
	@DisplayName("하나의 답변이라도 로그인 사용자와 다르면 false 반환")
	void allMatchLoginUserTest2() {
		// given
		User user = UserGenerator.questionWriter();
		Question question = QuestionGenerator.question(user);

		AnswerGenerator.answer(user, question, "answer1");
		AnswerGenerator.answer(UserGenerator.anotherUser(), question, "answer2");
		AnswerGenerator.answer(user, question, "answer3");

		// when
		Answers answers = Answers.of(question);

		// then
		assertThat(answers.allMatchLoginUser(user)).isFalse();
	}

}