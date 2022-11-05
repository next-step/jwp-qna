package qna.domain.message;

public class ExceptionMessage {

    public static final String NO_PERMISSION_DELETE_QUESTION = "질문을 삭제할 권한이 없습니다.";
    public static final String NO_PERMISSION_DELETE_ANSWER = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";
    public static final String INVALID_EMAIL_LENGTH = "이메일 길이는 50자 이내여야 합니다. (현재 = %d)";
    public static final String INVALID_NAME = "이름은 필수값입니다.";
    public static final String INVALID_NAME_LENGTH = "이름은 20자 이내여야 합니다. (현재 = %d)";
    public static final String INVALID_PASSWORD = "비밀번호는 필수값입니다.";
    public static final String INVALID_PASSWORD_LENGTH = "비밀번호는 20자 이내여야 합니다. (현재 = %d)";
    public static final String INVALID_TITLE = "제목은 필수값입니다.";
    public static final String INVALID_TITLE_LENGTH = "제목은 100자 이내여야 합니다. (현재 = %d)";
    public static final String INVALID_USER_ID = "사용자 ID는 필수값입니다.";
    public static final String INVALID_USER_ID_LENGTH = "사용자 ID는 20자 이내여야 합니다. (현재 = %d)";

    private ExceptionMessage() {
    }
}
