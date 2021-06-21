package qna.domain.exception.question;

import qna.domain.exception.DomainException;
import qna.domain.question.Question;

public class QuestionNotDeletedException extends DomainException {

	public QuestionNotDeletedException(Question question) {
		super("Question not deleted yet. " + question.getId());
	}
}
