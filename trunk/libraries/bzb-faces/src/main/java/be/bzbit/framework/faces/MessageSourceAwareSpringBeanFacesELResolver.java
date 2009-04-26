package be.bzbit.framework.faces;

import javax.el.ELContext;
import javax.el.ELException;
import javax.faces.context.FacesContext;

import org.springframework.context.MessageSource;
import org.springframework.web.jsf.el.SpringBeanFacesELResolver;

public class MessageSourceAwareSpringBeanFacesELResolver extends SpringBeanFacesELResolver {
	private static final Object[] NO_ARGS = new Object[] {};

	@Override
	public Object getValue(ELContext elContext, Object base, Object property)
			throws ELException {
		if (base instanceof MessageSource) {
			elContext.setPropertyResolved(true);
			FacesContext context = (FacesContext) elContext
					.getContext(FacesContext.class);
			return ((MessageSource) base).getMessage((String) property,
					NO_ARGS, context.getViewRoot().getLocale());
		} else {
			return super.getValue(elContext, base, property);
		}
	}
}
