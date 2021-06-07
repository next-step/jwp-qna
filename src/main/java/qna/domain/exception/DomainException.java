package qna.domain.exception;

/**
 *
 * @author heetaek.kim
 */
public abstract class DomainException extends Exception {

	public DomainException() {}

	public DomainException(String message) {
		super(message);
	}

	public DomainException(String message, Throwable cause) {
		super(message, cause);
	}
}
