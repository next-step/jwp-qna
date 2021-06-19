package qna.domain.exception.question;

import qna.domain.exception.DomainException;
import qna.domain.question.Answers;

public class AnswerNotDeletedException extends DomainException {

	private final Answers answers;

	public AnswerNotDeletedException(Answers answers) {
		this.answers = answers;
	}
}
