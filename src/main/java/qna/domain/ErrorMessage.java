package qna.domain;

public enum ErrorMessage {
    CANNOT_DELETE_QUESTION_EXCEPTION("질문을 삭제할 권한이 없습니다."),
    CANNOT_DELETE_ANSWER_EXCEPTION("질문을 삭제할 권한이 없습니다.");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
