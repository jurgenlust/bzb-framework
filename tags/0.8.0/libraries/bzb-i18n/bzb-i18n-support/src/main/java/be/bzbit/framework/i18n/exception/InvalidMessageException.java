package be.bzbit.framework.i18n.exception;

public class InvalidMessageException extends RuntimeException {

	private static final long serialVersionUID = 6394216744886192364L;

	public InvalidMessageException() {
		super();
	}

	public InvalidMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMessageException(String message) {
		super(message);
	}

	public InvalidMessageException(Throwable cause) {
		super(cause);
	}

}
