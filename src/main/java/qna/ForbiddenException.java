package qna;

public class ForbiddenException extends RuntimeException {
    private static final long serialVersionUID = -4969036082589231870L;

    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
