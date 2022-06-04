package qna;

public enum QnaExceptionType {
    EXIST_OTHER_ANSWER_ERROR("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다."),
    NOT_AUTHOR_QUESTION("질문을 삭제할 권한이 없습니다.");

    final String message;

    QnaExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
