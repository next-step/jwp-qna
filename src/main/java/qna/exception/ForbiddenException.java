package qna.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(ErrorMessages errorMessages) {
        super(errorMessages.getValues());
    }
}
