package qna;

public class CannotAddException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CannotAddException(String message) {
        super(message);
    }
}
