package qna.domain.exception;

public class EmailLengthExceedException extends IllegalArgumentException {
    private static final String EMAIL_LENGTH_EXCEED_ERROR = "email 주소는 %d자 이상을 초과할 수 없습니다.";

    public EmailLengthExceedException(final int lengthLimit) {
        super(String.format(EMAIL_LENGTH_EXCEED_ERROR, lengthLimit));
    }
}
