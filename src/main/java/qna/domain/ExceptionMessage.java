package qna.domain;

public enum ExceptionMessage {

    CANNOT_DELETE_NOT_OWNER("삭제할 권한이 없습니다."),
    CANNOT_DELETE_OTHERS_ANSWER("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
