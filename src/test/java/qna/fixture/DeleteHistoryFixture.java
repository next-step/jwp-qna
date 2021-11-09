package qna.fixture;

import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.Question;
import qna.domain.User;

public class DeleteHistoryFixture {
	public static DeleteHistory Q(Question question, User deleter) {
		return DeleteHistory.question(deleter, question);
	}

	public static DeleteHistory A(Answer answer, User deleter) {
		return DeleteHistory.answer(deleter, answer);
	}
}
