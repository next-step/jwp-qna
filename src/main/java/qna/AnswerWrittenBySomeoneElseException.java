package qna;

public class AnswerWrittenBySomeoneElseException extends RuntimeException{
    public static final String ERROR_WRITTEN_BY_SOMEONE_ELSE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    public AnswerWrittenBySomeoneElseException() {
        super(ERROR_WRITTEN_BY_SOMEONE_ELSE);
    }
}
