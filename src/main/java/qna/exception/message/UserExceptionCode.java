package qna.exception.message;

public enum UserExceptionCode {

    REQUIRED_USER_ID("사용자 ID는 반드시 입력돼야 합니다."),
    REQUIRED_PASSWORD("비밀번호는 반드시 입력돼야 합니다."),
    REQUIRED_NAME("사용자 이름은 반드시 입력돼야 합니다."),
    NOT_MATCH_USER_ID("사용자 ID가 일치하지 않습니다."),
    NOT_MATCH_PASSWORD("비밀번호가 일치하지 않습니다.");

    private static final String TITLE = "[ERROR] ";
    private String message;

    UserExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return TITLE + message;
    }
}
