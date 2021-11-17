package qna.common.exception;

public class BaseException extends RuntimeException {


    public BaseException() {
    }

    public BaseException(String errorMessage) {
        super(errorMessage);
    }

    public BaseException(ErrorMessage errorMessage) {
        super(errorMessage.getErrorMsg());
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }


    public BaseException(Throwable cause) {
        super(cause);
    }
}
