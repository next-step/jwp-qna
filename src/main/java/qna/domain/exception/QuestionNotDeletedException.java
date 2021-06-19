package qna.domain.exception;

import qna.domain.question.Question;

public class QuestionNotDeletedException extends DomainException {

	private final Question question;

	public QuestionNotDeletedException(Question question) {
		this.question = question;
	}
}
