package qna.constants;

public class ExceptionMessage {

    public static final String INVALID_DELETE_QUESTION_BECAUSE_NON_MATCH_WRITER_USER = "본인이 작성한 질문이 아니기 때문에 질문을 삭제 할 수 없습니다. (loginUser = %s)";
    public static final String INVALID_DELETE_QUESTION_BECAUSE_ANSWER_WRITER_NON_MATCH = "답변 작성자가 질문 작성자와 다르기 떄문에 질문을 삭제 할 수 없습니다. (loginUser = %s)";
}
