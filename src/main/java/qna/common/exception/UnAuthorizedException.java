package qna.common.exception;

public class UnAuthorizedException extends BaseException {

    public static final String UNAUTHORIZED_EXCEPTION_USER_ID_NOT_SAME_MESSAGE = "유저 ID가 일치하지 않습니다.";
    public static final String UNAUTHORIZED_EXCEPTION_MISS_MATCH_PASSWORD_MESSAGE = "비밀번호가 일치하지 않습니다.";

    public static final String GUEST_USER_NOT_QUESTION = "게스트는 질문을 작성 할 수 없습니다.";
    private static final long serialVersionUID = 1L;

    public UnAuthorizedException() {
        super();
    }

    public UnAuthorizedException(String errorMessage) {
        super(errorMessage);
    }

    public UnAuthorizedException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnAuthorizedException(Throwable cause) {
        super(cause);
    }
}
