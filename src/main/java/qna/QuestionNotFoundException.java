package qna;

public class QuestionNotFoundException extends RuntimeException {
	public QuestionNotFoundException(String message) {
		super(message);
	}

	public QuestionNotFoundException() {
		super(ErrorMessage.QUESTION_NOT_FOUND);
	}
}
