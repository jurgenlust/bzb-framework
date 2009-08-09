package be.bzbit.framework.domain.persistence.search;

import java.io.Serializable;

import be.bzbit.framework.domain.persistence.DAOCallback;
import be.bzbit.framework.domain.search.SearchCriteria;
import be.bzbit.framework.domain.search.SearchResult;

public interface SearchCriteriaHandler<T, ID extends Serializable> {
	SearchResult<T> find(SearchCriteria criteria);
	void iterate(SearchCriteria criteria, DAOCallback<T> callback);
}
