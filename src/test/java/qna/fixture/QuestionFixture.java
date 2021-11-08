package qna.fixture;

import qna.domain.Question;
import qna.domain.User;

public class QuestionFixture {
	public static Question Q1(User writer) {
		return new Question("title1", "contents1", writer.getId());
	}

	public static Question Q1(long id, User writer) {
		return new Question(id, "title1", "contents1").writeBy(writer);
	}
}
