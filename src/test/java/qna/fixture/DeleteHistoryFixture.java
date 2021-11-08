package qna.fixture;

import qna.domain.ContentType;
import qna.domain.DeleteHistory;

public class DeleteHistoryFixture {
	public static DeleteHistory Q(Long questionId, Long deletedById) {
		return new DeleteHistory(questionId, ContentType.QUESTION, deletedById);
	}

	public static DeleteHistory A(Long answerId, Long deletedById) {
		return new DeleteHistory(answerId, ContentType.ANSWER, deletedById);
	}
}
