package qna.exception.type;

public enum QuestionExceptionType {
    NOT_OWNER_LOGIN_USER("글을 작성한 사용자가 아닙니다"),
    ALREADY_DELETE_QUESTION("이미 삭제한 질문입니다.")
    ;

    private final String message;

    QuestionExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
