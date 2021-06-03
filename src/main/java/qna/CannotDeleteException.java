package qna;

import qna.domain.QnAExceptionMessage;

public class CannotDeleteException extends Exception {
    private static final long serialVersionUID = 1L;

    public CannotDeleteException(QnAExceptionMessage message) {
        super(message.message());
    }
}
