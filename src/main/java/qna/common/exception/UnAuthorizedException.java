package qna.common.exception;

public class UnAuthorizedException extends BaseException {

    private static final long serialVersionUID = 1L;

    public UnAuthorizedException() {
        super(ErrorMessage.UNAUTHORIZED_BASE_EXCEPTION_MESSAGE);
    }

    public UnAuthorizedException(ErrorMessage errorMessage) {
        super(errorMessage);
    }

    public UnAuthorizedException(String errorMessage) {
        super(errorMessage);
    }

    public UnAuthorizedException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnAuthorizedException(Throwable cause) {
        super(cause);
    }
}
