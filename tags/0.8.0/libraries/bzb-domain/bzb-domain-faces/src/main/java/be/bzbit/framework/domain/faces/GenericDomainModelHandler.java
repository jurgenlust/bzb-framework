package be.bzbit.framework.domain.faces;

import java.io.Serializable;

import java.util.List;

import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

import be.bzbit.framework.domain.model.DomainObject;
import be.bzbit.framework.domain.repository.DomainObjectRepository;


/**
 * Generic Handler for domain objects, providing a converter among others.
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 *
 * @param <T> the type of domain object
 */
public class GenericDomainModelHandler<T extends DomainObject>
        implements Serializable {
    //~ Static fields/initializers ---------------------------------------------

    private static final long serialVersionUID = -1762916104104529680L;

    //~ Instance fields --------------------------------------------------------

    private final DomainObjectConverter<T> converter;
    private DomainObjectRepository<T> repository;

    //~ Constructors -----------------------------------------------------------

    public GenericDomainModelHandler() {
        super();
        this.converter = new DomainObjectConverter<T>();
    }

    //~ Methods ----------------------------------------------------------------

    public DomainObjectRepository<T> getRepository() {
        return repository;
    }

    public void setRepository(final DomainObjectRepository<T> repository) {
        this.repository = repository;
        this.converter.setRepository(repository);
    }

    /**
     * Useful for small collections of domain objects: get a list of all instances,
     * wrapped in SelectItems.
     *
     * @return a list of SelectItems
     */
    public List<SelectItem> getSelectItems() {
        List<T> values = getRepository().findAll();

        return DomainModelSelectItemAdapter.getSelectItems(values);
    }

    /**
     * @return a DomainObject converter
     */
    public Converter getConverter() {
        return converter;
    }
}
