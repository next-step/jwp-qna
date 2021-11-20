package qna;

public class CannotDeleteSomeoneElseException extends CannotDeleteException{
    public static final String ERROR_WRITTEN_BY_SOMEONE_ELSE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    public CannotDeleteSomeoneElseException() {
        super(ERROR_WRITTEN_BY_SOMEONE_ELSE);
    }
}
