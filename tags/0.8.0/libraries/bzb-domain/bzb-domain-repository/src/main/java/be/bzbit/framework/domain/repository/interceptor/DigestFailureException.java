package be.bzbit.framework.domain.repository.interceptor;

public class DigestFailureException extends RuntimeException {

	private static final long serialVersionUID = -7833730047862420818L;

	public DigestFailureException() {
	}

	public DigestFailureException(String message) {
		super(message);
	}

	public DigestFailureException(Throwable cause) {
		super(cause);
	}

	public DigestFailureException(String message, Throwable cause) {
		super(message, cause);
	}

}
