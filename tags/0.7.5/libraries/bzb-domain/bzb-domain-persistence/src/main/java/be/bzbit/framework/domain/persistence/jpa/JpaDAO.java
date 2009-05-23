package be.bzbit.framework.domain.persistence.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Required;

import be.bzbit.framework.domain.persistence.DAO;
import be.bzbit.framework.domain.persistence.DAOCallback;
import be.bzbit.framework.domain.persistence.DAOInterceptor;
import be.bzbit.framework.domain.search.SearchCriteria;
import be.bzbit.framework.domain.search.SearchResult;

public class JpaDAO<T, ID extends Serializable> implements DAO<T, ID>,
		Serializable {

	private static final long serialVersionUID = 7701837250860002475L;
	private final Class<T> persistentClass;
	private final Class<ID> identifierClass;
	private final Set<DAOInterceptor<T>> interceptors; 
	
	public JpaDAO(Class<T> persistentClass, Class<ID> identifierClass) {
		super();
		this.persistentClass = persistentClass;
		this.identifierClass = identifierClass;
		this.interceptors = new LinkedHashSet<DAOInterceptor<T>>();
	}

	
	@SuppressWarnings("unchecked")
	public JpaDAO() {
		super();
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.identifierClass = (Class<ID>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
		this.interceptors = new LinkedHashSet<DAOInterceptor<T>>();
	}

	private EntityManager entityManager;

	public Criteria createCriteria() {
		final Session session = (Session) getEntityManager().getDelegate();
		return session.createCriteria(getPersistentClass());
	}
	
	@Override
	public long count() {
		return (Long) createCriteria().setProjection(Projections.rowCount()).list().get(0);
	}

	@Override
	public void delete(T persistentObject) {
		firePreDelete(persistentObject);
		getEntityManager().remove(persistentObject);
		firePostDelete(persistentObject);
	}

	@Override
	public void delete(ID id) {
		final T persistentObject = getEntityManager().find(persistentClass, id);
		firePreDelete(persistentObject);
		getEntityManager().remove(get(id));
		firePostDelete(persistentObject);
	}

	@Override
	public SearchResult<T> find(SearchCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T get(ID id) {
		final T persistentObject = getEntityManager().find(persistentClass, id);
		firePostLoad(persistentObject);
		return persistentObject;
	}

	@Override
	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@Override
	public void iterate(DAOCallback<T> callback) {
		iterateByCriteria(createCriteria(), callback);
	}
	
	@SuppressWarnings("unchecked")
	public void iterateByCriteria(Criteria criteria, DAOCallback<T> callback) {
		final ScrollableResults scrollableResults = criteria.scroll(ScrollMode.FORWARD_ONLY);
		final Session session = (Session)getEntityManager().getDelegate();
		while (scrollableResults.next()) {
			final T entity = (T)scrollableResults.get(0);
			firePostLoad(entity);
			callback.process(entity);
			//remove the entity from the session, so we don't get memory trouble
			session.evict(entity);
		}
	}

	@Override
	public void iterate(SearchCriteria criteria, DAOCallback<T> callback) {
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list() {
		final List<T> result = createCriteria().list();
		firePostLoad(result);
		return result;
	}

	@Override
	public void save(T persistentObject) {
		firePreSave(persistentObject);
		getEntityManager().merge(persistentObject);
		firePostSave(persistentObject);
	}


	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Required
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Class<ID> getIdentifierClass() {
		return identifierClass;
	}

	protected void firePreSave(T entity) {
		if (interceptors.isEmpty()) return;
		for (DAOInterceptor<T> interceptor : interceptors) {
			interceptor.preSave(entity);
		}
	}
	
	protected void firePostSave(T entity) {
		if (interceptors.isEmpty()) return;
		for (DAOInterceptor<T> interceptor : interceptors) {
			interceptor.postSave(entity);
		}
	}
	
	protected void firePreDelete(T entity) {
		if (interceptors.isEmpty()) return;
		for (DAOInterceptor<T> interceptor : interceptors) {
			interceptor.preDelete(entity);
		}
		
	}
	
	protected void firePostDelete(T entity) {
		if (interceptors.isEmpty()) return;
		for (DAOInterceptor<T> interceptor : interceptors) {
			interceptor.postDelete(entity);
		}
	
	}
	
	protected void firePostLoad(T entity) {
		if (interceptors.isEmpty()) return;
		for (DAOInterceptor<T> interceptor : interceptors) {
			interceptor.postLoad(entity);
		}
	}
	
	protected void firePostLoad(Collection<T> entities) {
		if (interceptors.isEmpty()) return;
		for (DAOInterceptor<T> interceptor : interceptors) {
			interceptor.postLoad(entities);
		}
	}	
	
	protected void firePostLoad(SearchResult<T> searchResult) {
		if (searchResult == null || searchResult.getList() == null || searchResult.getList().isEmpty()) return;
		if (interceptors.isEmpty()) return;
		for (DAOInterceptor<T> interceptor : interceptors) {
			interceptor.postLoad(searchResult.getList());
		}
	}
}
