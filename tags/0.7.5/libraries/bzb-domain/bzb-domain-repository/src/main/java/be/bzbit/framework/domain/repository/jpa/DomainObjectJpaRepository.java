package be.bzbit.framework.domain.repository.jpa;

import be.bzbit.framework.domain.model.DomainObject;
import be.bzbit.framework.domain.repository.DomainObjectRepository;


/**
 * JPA implementation of the DomainObjectRepository
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 *
 * @param <T> The persistent type
 */
public class DomainObjectJpaRepository<T extends DomainObject>
        extends GenericJpaRepository<T, Long>
        implements DomainObjectRepository<T> {
    //~ Constructors -----------------------------------------------------------

    public DomainObjectJpaRepository() {
        super();
    }

    public DomainObjectJpaRepository(final Class<T> entityClass) {
        super(entityClass);
    }
}
