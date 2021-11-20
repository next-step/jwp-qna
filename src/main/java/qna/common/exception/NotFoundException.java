package qna.common.exception;

public class NotFoundException extends BaseException {

    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public NotFoundException() {
        super(ErrorMessage.NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
