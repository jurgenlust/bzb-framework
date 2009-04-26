package be.bzbit.framework.exceptions;

/**
 * Business exception are meaningful to the business logic of an application
 * and should generate a userfriendly message in the UI.
 * 
 * The Business Exception Pattern is described here:
 * <a href="http://www.inze.be/andries/?p=41">http://www.inze.be/andries/?p=41</a>
 * 
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 *
 */
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 4903944353202394813L;

	/**
	 * A code indicating the cause of the exception.
	 */
	private String code;

	/**
	 * The exception that is wrapped by the business component.
	 */
	private Exception wrappedException;

	/**
	 * Constructor.
	 * 
	 * @param code
	 *            The code indicating the cause of the exception.
	 * @param wrappedException
	 *            The exception that is wrapped by the business component.
	 */
	public BusinessException(String code, Exception wrappedException) {
		this.code = code;
		this.wrappedException = wrappedException;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Exception getWrappedException() {
		return wrappedException;
	}

	public void setWrappedException(Exception wrappedException) {
		this.wrappedException = wrappedException;
	}

}
