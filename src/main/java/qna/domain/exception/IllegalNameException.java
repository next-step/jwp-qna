package qna.domain.exception;

public class IllegalNameException extends IllegalArgumentException {
    private static final String ILLEGAL_NAME_ERROR = "이름은 null이나 길이가 0일 수 없습니다.";

    public IllegalNameException() {
        super(ILLEGAL_NAME_ERROR);
    }

    public IllegalNameException(final String message) {
        super(message);
    }
}
