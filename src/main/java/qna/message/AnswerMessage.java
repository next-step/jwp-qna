package qna.message;

public enum AnswerMessage {
    ERROR_QUESTION_SHOULD_BE_NOT_NULL("질문이 없습니다."),
    ERROR_WRITER_SHOULD_BE_NOT_NULL("글쓴이는 필수입니다.");

    private final String message;

    AnswerMessage(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
