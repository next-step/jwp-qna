package qna.domain.exception;

public class IllegalDeleteHistoriesException extends IllegalArgumentException {
    private static final String ILLEGAL_DELETE_HISTORIES_ERROR = "삭제 내역은 null 일수 없습니다.";

    public IllegalDeleteHistoriesException() {
        super(ILLEGAL_DELETE_HISTORIES_ERROR);
    }

    public IllegalDeleteHistoriesException(final String message) {
        super(message);
    }

}
