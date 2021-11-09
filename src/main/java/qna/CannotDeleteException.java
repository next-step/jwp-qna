package qna;

import qna.domain.ErrorMessage;

public class CannotDeleteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CannotDeleteException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
