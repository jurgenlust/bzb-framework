package be.bzbit.framework.domain.repository.interceptor;

import java.util.Collection;

import be.bzbit.framework.domain.repository.Interceptor;

/**
 * Convenience class implementing the Interceptor methods. Extend this class
 * and override the methods you need
 *
 * @author jlust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 */
public class AbstractInterceptor<T> implements Interceptor<T> {
	
	private final Class<T> persistentClass;
	
	public AbstractInterceptor(Class<T> persistentClass) {
		super();
		this.persistentClass = persistentClass;
	}
	
	@Override
	public void postDelete(T entity) {
	}

	@Override
	public void postLoad(T entity) {
	}

	@Override
	public void postLoad(Collection<T> entities) {
	}

	@Override
	public void postSave(T entity) {
	}

	@Override
	public void preDelete(T entity) {
	}

	@Override
	public void preSave(T entity) {
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}
}
