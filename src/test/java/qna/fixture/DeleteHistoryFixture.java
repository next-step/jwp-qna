package qna.fixture;

import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;

public class DeleteHistoryFixture {
	public static DeleteHistory Q(Question question, User deleter) {
		return DeleteHistory.ofQuestion(deleter, question);
	}

	public static DeleteHistory A(Answer answer, User deleter) {
		return DeleteHistory.ofAnswer(deleter, answer);
	}
}
