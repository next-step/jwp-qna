package qna.domain.exception;

public class UserIdLengthExceedException extends IllegalArgumentException {
    private static final String USER_ID_LENGTH_EXCEED_ERROR = "사용자 ID는 %d자 이상을 초과할 수 없습니다.";

    public UserIdLengthExceedException(final int lengthLimit) {
        super(String.format(USER_ID_LENGTH_EXCEED_ERROR, lengthLimit));
    }
}
