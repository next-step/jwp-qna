package qna.exceptions;

public class StringTooLongException extends RuntimeException {

    public StringTooLongException(String message) {
        super(message);
    }
}
