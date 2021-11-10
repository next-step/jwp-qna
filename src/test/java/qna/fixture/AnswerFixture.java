package qna.fixture;

import qna.domain.Answer;
import qna.domain.User;

public class AnswerFixture {
	public static Answer A1(User writer) {
		return Answer.of(writer, "contents1");
	}

	public static Answer A1(Long id, User writer) {
		return Answer.of(id, writer, "contents1");
	}
}
