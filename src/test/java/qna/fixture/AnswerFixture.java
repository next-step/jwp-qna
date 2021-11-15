package qna.fixture;

import qna.domain.Answer;
import qna.domain.Question;

public class AnswerFixture {
	public static Answer 질문자와_답변자가_다른_답변(String writerId) {
		final Question question = QuestionFixture.식별자가_userId인_유저가_작성한_질문(String.format("question.%s", writerId));
		return 질문과_userId로_식별되는_작성자를_갖는_답변(question, writerId);
	}

	public static Answer 질문과_userId로_식별되는_작성자를_갖는_답변(Question question, String writerId) {
		final Answer answer = new Answer(UserFixture.식별자가_userId인_유저(writerId), question, "answer.contents");
		question.addAnswer(answer);
		return answer;
	}
}
