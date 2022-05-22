package qna;

public class CannotDeleteException extends RuntimeException {
    private static final long serialVersionUID = 2288205147413093974L;

    public CannotDeleteException() {
        super();
    }

    public CannotDeleteException(String message) {
        super(message);
    }
}
