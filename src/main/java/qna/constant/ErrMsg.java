package qna.constant;

public class ErrMsg {


    private ErrMsg() {
        throw new AssertionError();
    }

    public static final String CANNOT_DELETE_WRONG_USER = "삭제할 권한이 없습니다. 작성자만 삭제가능합니다.";
    public static final String CANNOT_DELETE_ALREADY_DELETED = "이미 삭제되었습니다.";

    public static final String CANNOT_UPDATE_DUPLICATED = "중복된 답변을 추가할 수 없습니다.";

    public static final String CANNOT_ADD_ANSWER_TO_WRONG_QUESTION = "답변이 의도한 질문과 추가하려는 질문이 일치하지 않습니다.";

    public static final String CANNOT_ADD_DELETE_HISTORY_TO_WRONG_USER = "삭제 히스토리를 잘못된 사용자에게 추가하려고 합니다.";
}
