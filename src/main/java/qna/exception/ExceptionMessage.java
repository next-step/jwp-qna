package qna.exception;

public enum ExceptionMessage {
    CANNOT_DELETE_QUESTION_MESSAGE("질문을 삭제할 권한이 없습니다."),
    CANNOT_DELETE_ANSWER_DIFFERENT_USER_MESSAGE("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다."),
    VALIDATE_TITLE_MESSAGE("제목은 1자 이상 100자 이하로 작성해야 합니다."),
    VALIDATE_USER_ID_MESSAGE("사용자 아이디는 1자 이상 20자 이하로 입력해 주세요."),
    VALIDATE_PASSWORD_MESSAGE("비밀번호는 1자 이상 20자 이하로 입력해 주세요."),
    VALIDATE_USERNAME_MESSAGE("사용자 이름은 1자 이상 20자 이하로 입력해 주세요."),
    VALIDATE_EMAIL_MESSAGE("이메일은 50자 이하로 입력해 주세요.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
