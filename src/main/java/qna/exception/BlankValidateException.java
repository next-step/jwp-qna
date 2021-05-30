package qna.exception;

public class BlankValidateException extends RuntimeException {
    private static final String MESSAGE = "값이 비어있습니다.";

    public BlankValidateException() {
        super(MESSAGE);
    }

    public BlankValidateException(String field, String value) {
        super(MESSAGE + " 필드:" + field + ", 값:" + value);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
