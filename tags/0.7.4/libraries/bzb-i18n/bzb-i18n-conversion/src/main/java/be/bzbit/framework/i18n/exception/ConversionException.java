package be.bzbit.framework.i18n.exception;

public class ConversionException extends RuntimeException {

	private static final long serialVersionUID = -4313193753409438198L;

	public ConversionException() {
		super();
	}

	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConversionException(String message) {
		super(message);
	}

	public ConversionException(Throwable cause) {
		super(cause);
	}

}
