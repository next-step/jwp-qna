package qna.exception;

public enum ExceptionMessage {
    CANNOT_DELETE_EXCEPTION_MESSAGE("질문을 삭제할 권한이 없습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
