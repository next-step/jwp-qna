package qna.common;

public class ErrorMessage {
    public static final String WRITTEN_BY_SOMEONE_ELSE_ANSWER = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";
    public static final String NOT_PERMISSION_DELETE_QUESTION = "질문을 삭제할 권한이 없습니다.";
    public static final String ID_DOES_NOT_EXIST = "아이디가 존재하지 않습니다.";
    public static final String PASSWORD_DOES_NOT_EXIST = "비밀번호가 존재하지 않습니다.";
    public static final String ID_OR_PASSWORD_NOT_MATCH = "아이디 또는 비밀번호가 일치하지 않습니다.";
    public static final String ALREADY_DELETED = "이미 삭제된 질문입니다.";

    private ErrorMessage() {
    }
}
