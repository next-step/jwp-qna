package qna.message;

public enum UserMessage {
    ERROR_USER_ID_SHOULD_BE_NOT_NULL("Id는 필수값입니다."),
    ERROR_PASSWORD_SHOULD_BE_NOT_NULL("패스워드는 필수값입니다."),
    ERROR_NAME_SHOULD_BE_NOT_NULL("이름은 필수값입니다.");

    private final String message;

    UserMessage(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
