package be.bzbit.framework.domain.repository;

public interface GenericRepositoryCallback<T> {
	public void process(T entity);
}
