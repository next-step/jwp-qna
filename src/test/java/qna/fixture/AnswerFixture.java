package qna.fixture;

import qna.domain.Answer;

public class AnswerFixture {
	public static Answer A1(Long questionId, Long writerId) {
		return new Answer("contents", false, questionId, writerId);
	}
}
