package be.bzbit.framework.pdf.exception;

public class PdfRenderingException extends RuntimeException {

	private static final long serialVersionUID = 6042136295924470778L;

	public PdfRenderingException() {
	}

	public PdfRenderingException(String message) {
		super(message);
	}

	public PdfRenderingException(Throwable cause) {
		super(cause);
	}

	public PdfRenderingException(String message, Throwable cause) {
		super(message, cause);
	}

}
