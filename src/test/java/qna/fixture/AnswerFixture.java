package qna.fixture;

import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

public class AnswerFixture {
	public static Answer A1(User writer, Question question) {
		return Answer.of(writer, question, "contents");
	}

	public static Answer A1(Long id, User writer, Question question) {
		return Answer.of(id, writer, question, "contents");
	}
}
