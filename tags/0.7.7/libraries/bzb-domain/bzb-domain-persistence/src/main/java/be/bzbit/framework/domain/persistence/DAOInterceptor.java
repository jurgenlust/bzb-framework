package be.bzbit.framework.domain.persistence;

import java.util.Collection;

public interface DAOInterceptor<T> {
	void preSave(T entity);
	void postSave(T entity);
	void preDelete(T entity);
	void postDelete(T entity);
	void postLoad(T entity);
	void postLoad(Collection<T> entities);
}
