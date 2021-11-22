package qna;

public class NoPermissionDeleteQuestionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public static final String ERROR_PERMISSION_TO_DELETE = "질문을 삭제할 권한이 없습니다.";

    public NoPermissionDeleteQuestionException() {
        super(ERROR_PERMISSION_TO_DELETE);
    }
}
