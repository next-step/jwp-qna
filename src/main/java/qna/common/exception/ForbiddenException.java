package qna.common.exception;

public class ForbiddenException extends BaseException {

    public ForbiddenException() {
    }

    public ForbiddenException(ErrorMessage message) {
        super(message);
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
