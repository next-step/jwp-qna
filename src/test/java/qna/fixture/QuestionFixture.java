package qna.fixture;

import qna.domain.Question;
import qna.domain.User;

public class QuestionFixture {
	public static Question Q1(User writer) {
		return Question.of(writer, "title1", "contents1");
	}

	public static Question Q1(long id, User writer) {
		return Question.of(id, writer, "title1", "contents1");
	}
}
