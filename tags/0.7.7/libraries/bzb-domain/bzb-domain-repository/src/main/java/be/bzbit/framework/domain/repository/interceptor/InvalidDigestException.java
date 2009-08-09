package be.bzbit.framework.domain.repository.interceptor;

public class InvalidDigestException extends RuntimeException {

	private static final long serialVersionUID = 3853945761149195722L;

	public InvalidDigestException() {
		super();
	}

	public InvalidDigestException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDigestException(String message) {
		super(message);
	}

	public InvalidDigestException(Throwable cause) {
		super(cause);
	}

}
