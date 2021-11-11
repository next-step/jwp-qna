package qna.fixture;

import java.util.Arrays;
import java.util.List;

import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

public class QuestionFixture {
	public static Question Q1(User writer) {
		return Question.of(1L, writer, "title1", "contents1");
	}

	public static Question SELF_ANSWERED_Q(User writer) {
		Question question = Question.of(10L, writer, "title10", "contents10");
		List<Answer> answers = Arrays.asList(AnswerFixture.A1(writer), AnswerFixture.A2(writer));
		answers.forEach(question::addAnswer);
		return question;
	}
}
