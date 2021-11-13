package qna.fixture;

import qna.domain.Answer;
import qna.domain.User;

public class AnswerFixture {
	public static Answer A1(User writer) {
		return Answer.of(1L, writer, "contents1");
	}

	public static Answer A2(User writer) {
		return Answer.of(2L, writer, "contents2");
	}

	public static Answer A3(User writer) {
		return Answer.of(3L, writer, "contents3");
	}
}
