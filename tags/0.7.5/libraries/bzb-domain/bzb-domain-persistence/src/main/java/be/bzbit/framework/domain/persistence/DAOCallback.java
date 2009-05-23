package be.bzbit.framework.domain.persistence;

public interface DAOCallback<T> {
	void process(T persistentObject);
}
