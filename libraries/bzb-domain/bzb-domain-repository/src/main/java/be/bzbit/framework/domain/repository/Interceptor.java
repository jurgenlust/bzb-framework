package be.bzbit.framework.domain.repository;

import java.util.Collection;

/**
 * Interceptor for Generic Repository operations
 *
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 *
 * @version $LastChangedRevision: 258 $
 *
 * @param <T> the entity type
 */
public interface Interceptor<T> {
	void preSave(T entity);
	void postSave(T entity);
	void preDelete(T entity);
	void postDelete(T entity);
	void postLoad(T entity);
	void postLoad(Collection<T> entities);
}
