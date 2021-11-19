package qna.domain.exception;

public class NameLengthExceedException extends IllegalArgumentException {
    private static final String NAME_LENGTH_EXCEED_ERROR = "사용자 이름은 %d자 이상을 초과할 수 없습니다.";

    public NameLengthExceedException(final int lengthLimit) {
        super(String.format(NAME_LENGTH_EXCEED_ERROR, lengthLimit));
    }
}
