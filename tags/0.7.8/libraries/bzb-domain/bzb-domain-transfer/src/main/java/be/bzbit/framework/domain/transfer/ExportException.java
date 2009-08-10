package be.bzbit.framework.domain.transfer;

/**
 * An Exception thrown when something goes wrong while exporting a repository
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 */
public class ExportException extends RuntimeException {

	private static final long serialVersionUID = -2437051651226287465L;

	public ExportException() {
		super();
	}

	public ExportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExportException(String message) {
		super(message);
	}

	public ExportException(Throwable cause) {
		super(cause);
	}

}
