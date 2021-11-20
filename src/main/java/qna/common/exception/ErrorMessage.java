package qna.common.exception;

public enum ErrorMessage {
    ERROR_INVALID_EXCEPTION_MESSAGE("잘못된 입력값이 입력되었습니다."),
    UNAUTHORIZED_EXCEPTION_USER_ID_NOT_SAME_EXCEPTION_MESSAGE("유저 ID가 일치하지 않습니다."),
    UNAUTHORIZED_EXCEPTION_MISS_MATCH_PASSWORD_EXCEPTION_MESSAGE("비밀번호가 일치하지 않습니다."),
    GUEST_USER_NOT_QUESTION_EXCEPTION_MESSAGE("게스트는 질문을 작성 할 수 없습니다."),

    NOT_FOUND_EXCEPTION_MESSAGE("not found exception"),

    UNAUTHORIZED_BASE_EXCEPTION_MESSAGE("인증 실패");

    private final String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMsg(Object... arg) {
        return String.format(errorMessage, arg);
    }
}

