package qna.message;

public enum DeleteHistoryMessage {
    ERROR_CONTENT_ID_SHOULD_BE_NOT_NULL("삭제하는 대상의 ID 값은 필수입니다.");

    private final String message;

    DeleteHistoryMessage(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
