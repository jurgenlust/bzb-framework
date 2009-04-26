package be.bzbit.framework.domain.persistence;

import java.io.Serializable;
import java.util.List;

import be.bzbit.framework.domain.search.SearchCriteria;
import be.bzbit.framework.domain.search.SearchResult;

public interface DAO<T, ID extends Serializable> {
	Class<T> getPersistentClass();
	Class<ID> getIdentifierClass();
	T get(ID id);
	List<T> list();
	void iterate(DAOCallback<T> callback);
	SearchResult<T> find(SearchCriteria criteria);
	void iterate(SearchCriteria criteria, DAOCallback<T> callback);
	long count();
	void save(T persistentObject);
	void delete(T persistentObject);
	void delete(ID id);
}
