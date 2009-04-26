package be.bzbit.framework.i18n.exception;

public class LocaleNotAvailableException extends RuntimeException {

	private static final long serialVersionUID = 63687412599273702L;

	public LocaleNotAvailableException() {
		super();
	}

	public LocaleNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public LocaleNotAvailableException(String message) {
		super(message);
	}

	public LocaleNotAvailableException(Throwable cause) {
		super(cause);
	}

}
