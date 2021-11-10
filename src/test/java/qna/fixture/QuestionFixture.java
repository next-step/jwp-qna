package qna.fixture;

import qna.domain.Question;
import qna.domain.User;

public class QuestionFixture {
	public static Question Q1(User writer) {
		return Question.of(1L, writer, "title1", "contents1");
	}
}
