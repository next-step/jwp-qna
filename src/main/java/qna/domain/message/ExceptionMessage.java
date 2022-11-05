package qna.domain.message;

public class ExceptionMessage {

    public static final String NO_PERMISSION_DELETE_QUESTION = "질문을 삭제할 권한이 없습니다.";
    public static final String NO_PERMISSION_DELETE_ANSWER = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    private ExceptionMessage() {
    }
}
