package qna;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5387438776687056655L;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
