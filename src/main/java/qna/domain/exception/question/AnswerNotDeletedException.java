package qna.domain.exception.question;

import qna.domain.exception.DomainException;

public class AnswerNotDeletedException extends DomainException {

	public AnswerNotDeletedException() {
		super("Answers has not deleted answer.");
	}
}
