package be.bzbit.framework.domain.persistence.search.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Required;

import be.bzbit.framework.domain.persistence.DAOCallback;
import be.bzbit.framework.domain.persistence.search.SearchCriteriaHandler;
import be.bzbit.framework.domain.search.SearchCriteria;
import be.bzbit.framework.domain.search.SearchResult;

public class NamedQuerySearchCriteriaHandler<T, ID extends Serializable>
		implements Serializable, SearchCriteriaHandler<T, ID> {

	private static final long serialVersionUID = 4236519949975878604L;
	private EntityManager entityManager;

	@Override
	public SearchResult<T> find(SearchCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void iterate(SearchCriteria criteria, DAOCallback<T> callback) {
		// TODO Auto-generated method stub
	}

	@Required
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

}
