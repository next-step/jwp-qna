package qna;

public class CannotDeleteException extends Exception {
    private static final long serialVersionUID = 1L;

    public CannotDeleteException(QnaExceptionType type) {
        super(type.getMessage());
    }

    public CannotDeleteException(String message) {
        super(message);
    }
}
