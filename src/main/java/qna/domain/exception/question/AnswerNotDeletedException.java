package qna.domain.exception.question;

import qna.domain.exception.DomainException;
import qna.domain.question.AnswerList;

public class AnswerNotDeletedException extends DomainException {

	private final AnswerList answerList;

	public AnswerNotDeletedException(AnswerList answerList) {
		this.answerList = answerList;
	}
}
