package be.bzbit.framework.webflow.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.engine.RequestControlContext;
import org.springframework.webflow.execution.FlowExecutionException;

import be.bzbit.framework.exceptions.BusinessException;

/**
 * FlowExecutionExceptionHandler that adds an error message to the MessageContext
 * when a BusinessException is caught.
 * 
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 *
 * @version $LastChangedRevision: 216 $
 *
 */
public class BusinessExceptionHandler implements FlowExecutionExceptionHandler {
	private static final Log log = LogFactory.getLog(BusinessExceptionHandler.class);

	@Override
	public boolean canHandle(FlowExecutionException exception) {
		if (findBusinessException(exception) != null) {
			return true;
		} else {
			return false;
		}
	}

	private BusinessException findBusinessException(
			FlowExecutionException exception) {
		Throwable cause = exception.getCause();
		while (cause != null) {
			if (cause instanceof BusinessException) {
				return (BusinessException) cause;
			}
			cause = cause.getCause();
		}
		return null;
	}

	@Override
	public void handle(FlowExecutionException ex, RequestControlContext context) {
		final String code = findBusinessException(ex).getCode();
		log.error("Business Error code " + code);

		context.getMessageContext().addMessage(
				new MessageBuilder().error().source(null).code("exception." + code)
						.defaultText("Error: " + code).build());

		context.getExternalContext().requestFlowExecutionRedirect();

	}

}
