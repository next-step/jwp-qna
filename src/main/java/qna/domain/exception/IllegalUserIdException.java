package qna.domain.exception;

public class IllegalUserIdException extends IllegalArgumentException {
    private static final String ILLEGAL_USER_ID_ERROR = "사용자 ID는 null이나 길이가 0일 수 없습니다.";

    public IllegalUserIdException() {
        super(ILLEGAL_USER_ID_ERROR);
    }

    public IllegalUserIdException(final String message) {
        super(message);
    }
}
