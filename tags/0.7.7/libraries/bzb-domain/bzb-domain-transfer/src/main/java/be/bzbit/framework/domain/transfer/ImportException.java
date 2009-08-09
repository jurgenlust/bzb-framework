package be.bzbit.framework.domain.transfer;

import java.io.Serializable;

public class ImportException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = -5975615411451689418L;

	public ImportException() {
		super();
	}

	public ImportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImportException(String message) {
		super(message);
	}

	public ImportException(Throwable cause) {
		super(cause);
	}

}
