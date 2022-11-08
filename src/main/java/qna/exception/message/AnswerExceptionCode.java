package qna.exception.message;

public enum AnswerExceptionCode {
    REQUIRED_WRITER("답변자는 반드시 입력돼야 합니다."),
    REQUIRED_QUESTION("질문은 반드시 입력돼야 합니다."),
    ALREADY_DELETED("이미 삭제된 답변입니다."),
    NOT_MATCH_LOGIN_USER("로그인한 사용자와 답변자가 일치하지 않습니다.");

    private static final String TITLE = "[ERROR] ";
    private String message;

    AnswerExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}
