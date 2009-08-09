/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.bzbit.framework.pdf.exception;

/**
 *
 * @author jlust
 */
public class PdfBuilderException extends RuntimeException {

	private static final long serialVersionUID = -6656052390263577433L;

	public PdfBuilderException(Throwable cause) {
        super(cause);
    }

    public PdfBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PdfBuilderException(String message) {
        super(message);
    }

    public PdfBuilderException() {
    }

}
