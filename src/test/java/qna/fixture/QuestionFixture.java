package qna.fixture;

import qna.domain.Question;

public class QuestionFixture {
	public static Question Q1(Long writerId) {
		return new Question("title1", "contents1", writerId);
	}
}
