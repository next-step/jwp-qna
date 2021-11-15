package qna.fixture;

import qna.domain.Question;

public class QuestionFixture {
	public static Question 식별자가_userId인_유저가_작성한_질문(String writerId) {
		final Question question = new Question("title", "question.contents");
		question.writeBy(UserFixture.식별자가_userId인_유저(writerId));
		return question;
	}
}
