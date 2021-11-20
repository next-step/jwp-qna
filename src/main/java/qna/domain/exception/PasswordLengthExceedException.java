package qna.domain.exception;

public class PasswordLengthExceedException extends IllegalArgumentException {
    private static final String PASSWORD_LENGTH_EXCEED_ERROR = "사용자 ID는 %d자 이상을 초과할 수 없습니다.";

    public PasswordLengthExceedException(final int lengthLimit) {
        super(String.format(PASSWORD_LENGTH_EXCEED_ERROR, lengthLimit));
    }
}
