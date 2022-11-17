package qna.message;

public enum AnswerMessage {
    ERROR_QUESTION_SHOULD_BE_NOT_NULL("질문이 없습니다."),
    ERROR_WRITER_SHOULD_BE_NOT_NULL("글쓴이는 필수입니다."),
    ERROR_CAN_NOT_DELETE_IF_OWNER_NOT_EQUALS("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");

    private final String message;

    AnswerMessage(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
