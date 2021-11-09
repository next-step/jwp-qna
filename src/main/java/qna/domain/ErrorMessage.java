package qna.domain;

public enum ErrorMessage {

    DELETE_QUESTION_NOT_ALLOWED("질문을 삭제할 권한이 없습니다."),
    EXISTS_ANSWER_OF_OTHER("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
