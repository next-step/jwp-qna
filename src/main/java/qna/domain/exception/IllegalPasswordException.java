package qna.domain.exception;

public class IllegalPasswordException extends IllegalArgumentException {
    private static final String ILLEGAL_PASSWORD_ERROR = "비밀번호는 null이나 길이가 0일 수 없습니다.";

    public IllegalPasswordException() {
        super(ILLEGAL_PASSWORD_ERROR);
    }

    public IllegalPasswordException(final String message) {
        super(message);
    }
}
