package qna.exception.type;

public enum NotFoundExceptionType {

    NOT_FOUND_QUESTION("질문을 찾을 수 없습니다"),
    NOT_FOUND_ANSWER("답변을 찾을 수 없습니다.");

    private final String message;

    NotFoundExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
