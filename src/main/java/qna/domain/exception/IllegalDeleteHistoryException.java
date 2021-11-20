package qna.domain.exception;

public class IllegalDeleteHistoryException extends IllegalArgumentException {
    private static final String ILLEGAL_DELETE_HISTORY_ERROR = "삭제 내역은 null 일수 없습니다.";

    public IllegalDeleteHistoryException() {
        super(ILLEGAL_DELETE_HISTORY_ERROR);
    }

    public IllegalDeleteHistoryException(final String message) {
        super(message);
    }

}
