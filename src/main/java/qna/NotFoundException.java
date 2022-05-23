package qna;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = -5303571215712669727L;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
