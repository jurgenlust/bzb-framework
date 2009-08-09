package be.bzbit.framework.domain.faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import be.bzbit.framework.domain.model.DomainObject;
import be.bzbit.framework.domain.repository.DomainObjectRepository;


public class DomainObjectConverter<T extends DomainObject>
        implements Converter {
    //~ Instance fields --------------------------------------------------------

    private DomainObjectRepository<T> repository;

    //~ Methods ----------------------------------------------------------------

    public DomainObjectRepository<T> getRepository() {
        return repository;
    }

    public void setRepository(final DomainObjectRepository<T> repository) {
        this.repository = repository;
    }

    public Object getAsObject(
        final FacesContext context,
        final UIComponent component,
        final String valueAsString
    )
            throws ConverterException {
        if ((valueAsString == null) || (valueAsString.trim().length() == 0)) {
            return null;
        }

        try {
            Long id = Long.valueOf(valueAsString);

            return getRepository().findById(id);
        } catch (final Exception e) {
            throw new ConverterException(e);
        }
    }

    public String getAsString(
        final FacesContext context,
        final UIComponent component,
        final Object valueAsObject
    )
            throws ConverterException {
        if (valueAsObject == null) {
            return "";
        }

        try {
            DomainObject domainObject = (DomainObject) valueAsObject;

            return String.valueOf(domainObject.getId());
        } catch (final Exception e) {
            throw new ConverterException(e);
        }
    }
}
