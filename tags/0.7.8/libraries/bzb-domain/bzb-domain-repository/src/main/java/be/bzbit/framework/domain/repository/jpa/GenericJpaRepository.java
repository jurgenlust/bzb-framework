package be.bzbit.framework.domain.repository.jpa;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Required;

import be.bzbit.framework.domain.repository.GenericRepository;
import be.bzbit.framework.domain.repository.GenericRepositoryCallback;
import be.bzbit.framework.domain.repository.Interceptor;
import be.bzbit.framework.domain.repository.Sort;
import be.bzbit.framework.domain.repository.SortField;
import be.bzbit.framework.domain.repository.search.EjbQLSearchCriteria;
import be.bzbit.framework.domain.repository.search.FullTextSearchCriteria;
import be.bzbit.framework.domain.repository.search.NamedQuerySearchCriteria;
import be.bzbit.framework.domain.repository.search.SearchCriteria;
import be.bzbit.framework.domain.repository.search.SearchResult;

/**
 * JPA implementation of the GenericRepository. Note that this implementation
 * also expects Hibernate als JPA implementation.
 * 
 * @author Jurgen Lust
 * @author $LastChangedBy$
 * 
 * @version $LastChangedRevision$
 * 
 * @param <T>
 *            The persistent type
 * @param <ID>
 *            The primary key type
 */
public class GenericJpaRepository<T, ID extends Serializable> implements
		GenericRepository<T, ID> {

	private static final Log log = LogFactory
			.getLog(GenericJpaRepository.class);

	// ~ Instance fields
	// --------------------------------------------------------

	private final Class<T> persistentClass;
	private final Set<Interceptor<T>> interceptors;
	private EntityManager entityManager;
	private int indexBatchSize = 25;

	// ~ Constructors
	// -----------------------------------------------------------

	@SuppressWarnings("unchecked")
	public GenericJpaRepository() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.interceptors = new LinkedHashSet<Interceptor<T>>();
	}

	public GenericJpaRepository(final Class<T> persistentClass) {
		super();
		this.persistentClass = persistentClass;
		this.interceptors = new LinkedHashSet<Interceptor<T>>();
	}

	// ~ Methods
	// ----------------------------------------------------------------

	public int getIndexBatchSize() {
		return indexBatchSize;
	}

	public void setIndexBatchSize(final int indexBatchSize) {
		this.indexBatchSize = indexBatchSize;
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#countAll()
	 */
	@Override
	public int countAll() {
		return countByCriteria();
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#countByExample(java.lang.Object)
	 */
	@Override
	public int countByExample(final T exampleInstance) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		crit.setProjection(Projections.rowCount());
		crit.add(Example.create(exampleInstance));

		return (Integer) crit.list().get(0);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findAll()
	 */
	@Override
	public List<T> findAll() {
		return findByCriteria();
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findAll(int, int)
	 */
	@Override
	public List<T> findAll(final int firstResult, final int maxResults) {
		return findByCriteria(firstResult, maxResults);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findAll(int, int, be.bzbit.framework.domain.repository.Sort)
	 */
	@Override
	public List<T> findAll(final int firstResult, final int maxResults,
			final Sort sort) {
		return findByCriteria(firstResult, maxResults, sort);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findByExample(java.lang.Object)
	 */
	@Override
	public List<T> findByExample(final T exampleInstance) {
		return findByExample(exampleInstance, -1, -1);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findByExample(java.lang.Object, be.bzbit.framework.domain.repository.Sort)
	 */
	@Override
	public List<T> findByExample(final T exampleInstance, final Sort sort) {
		return findByExample(exampleInstance, -1, -1, sort);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findByExample(java.lang.Object, int, int)
	 */
	@Override
	public List<T> findByExample(final T exampleInstance,
			final int firstResult, final int maxResults) {
		return findByExample(exampleInstance, firstResult, maxResults, null);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findByExample(java.lang.Object, int, int, be.bzbit.framework.domain.repository.Sort)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByExample(final T exampleInstance,
			final int firstResult, final int maxResults, final Sort sort) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		addOrder(crit, sort);

		crit.add(Example.create(exampleInstance));

		if (firstResult > 0) {
			crit.setFirstResult(firstResult);
		}

		if (maxResults > 0) {
			crit.setMaxResults(maxResults);
		}
		
		final List<T> result = crit.list();  
		firePostLoad(result);
		return result; 
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findById(java.io.Serializable)
	 */
	@Override
	public T findById(final ID id) {
		final T result = getEntityManager().find(persistentClass, id);
		firePostLoad(result);
		return result;
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findById(java.io.Serializable, boolean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T findById(final ID id, final boolean lock) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		crit.add(Restrictions.idEq(id));
		crit.setLockMode(LockMode.UPGRADE);

		final T result = (T) crit.uniqueResult();
		firePostLoad(result);
		return result;
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findByNamedQuery(java.lang.String, java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedQuery(final String name, Object... params) {
		javax.persistence.Query query = getEntityManager().createNamedQuery(
				name);

		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}

		final List<T> result = (List<T>) query.getResultList();
		firePostLoad(result);
		return result;
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findByNamedQueryAndNamedParams(java.lang.String, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedQueryAndNamedParams(final String name,
			final Map<String, ? extends Object> params) {
		javax.persistence.Query query = getEntityManager().createNamedQuery(
				name);

		for (final Map.Entry<String, ? extends Object> param : params
				.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}

		final List<T> result = (List<T>) query.getResultList();
		firePostLoad(result);
		return result;
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#findall(be.bzbit.framework.domain.repository.Sort)
	 */
	@Override
	public List<T> findall(final Sort sort) {
		return findByCriteria(sort);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#getEntityClass()
	 */
	@Override
	public Class<T> getEntityClass() {
		return persistentClass;
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#makePersistent(java.lang.Object)
	 */
	public T makePersistent(final T instance) {
		return save(instance);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#makeTransient(java.lang.Object)
	 */
	@Override
	public void makeTransient(final T instance) {
		delete(instance);
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@Required
	@PersistenceContext
	public void setEntityManager(final EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	protected List<T> findByCriteria(final Criterion... criterion) {
		return findByCriteria(-1, -1, null, criterion);
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	protected List<T> findByCriteria(final Sort sort,
			final Criterion... criterion) {
		return findByCriteria(-1, -1, sort, criterion);
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	protected List<T> findByCriteria(final int firstResult,
			final int maxResults, final Criterion... criterion) {
		return findByCriteria(firstResult, maxResults, null, criterion);
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(final int firstResult,
			final int maxResults, final Sort sort, final Criterion... criterion) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		addOrder(crit, sort);

		for (final Criterion c : criterion) {
			crit.add(c);
		}

		if (firstResult > 0) {
			crit.setFirstResult(firstResult);
		}

		if (maxResults > 0) {
			crit.setMaxResults(maxResults);
		}

		final List<T> result = crit.list();
		firePostLoad(result);
		return result;
	}

	protected int countByCriteria(Criterion... criterion) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		crit.setProjection(Projections.rowCount());

		for (final Criterion c : criterion) {
			crit.add(c);
		}

		return (Integer) crit.list().get(0);
	}

	private void addOrder(final Criteria criteria, final Sort sort) {
		if ((criteria == null) || (sort == null) || sort.getFields().isEmpty()) {
			return;
		}

		for (final SortField field : sort.getFields()) {
			switch (field.getDirection()) {
			case ASC:
				criteria.addOrder(Order.asc(field.getPropertyName()));

				break;

			case DESC:
				criteria.addOrder(Order.desc(field.getPropertyName()));

				break;
			}
		}
	}

	private void addOrder(final FullTextQuery query, final Sort sort) {
		if ((query == null) || (sort == null) || sort.getFields().isEmpty()) {
			return;
		}

		org.apache.lucene.search.Sort luceneSort = new org.apache.lucene.search.Sort();
		List<org.apache.lucene.search.SortField> luceneSortFields = new ArrayList<org.apache.lucene.search.SortField>();

		for (final SortField field : sort.getFields()) {
			luceneSortFields.add(new org.apache.lucene.search.SortField(field
					.getPropertyName(),
					field.getDirection() != Sort.Direction.ASC));
		}

		luceneSort
				.setSort(luceneSortFields
						.toArray(new org.apache.lucene.search.SortField[luceneSortFields
								.size()]));
		query.setSort(luceneSort);
	}

	/**
	 * initial build of the Hibernate Search index
	 */
	public void buildIndex() {
		log.info("Building full text search index");
		Session session = (Session) getEntityManager().getDelegate();
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		fullTextSession.setFlushMode(FlushMode.MANUAL);
		fullTextSession.setCacheMode(CacheMode.IGNORE);

		Transaction transaction = fullTextSession.beginTransaction();

		// Scrollable results will avoid loading too many objects in memory
		ScrollableResults results = fullTextSession.createCriteria(
				persistentClass).scroll(ScrollMode.FORWARD_ONLY);
		int index = 0;

		while (results.next()) {
			index++;
			fullTextSession.index(results.get(0)); // index each element

			if ((index % getIndexBatchSize()) == 0) {
				fullTextSession.clear(); // clear every batchSize since the
				// queue is processed
			}
		}

		transaction.commit();
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#find(be.bzbit.framework.domain.repository.search.SearchCriteria)
	 */
	@Override
	public SearchResult<T> find(SearchCriteria criteria) {
		if (criteria instanceof FullTextSearchCriteria) {
			return find((FullTextSearchCriteria) criteria);
		} else if (criteria instanceof NamedQuerySearchCriteria) {
			return find((NamedQuerySearchCriteria) criteria);
		} else if (criteria instanceof EjbQLSearchCriteria) {
			return find((EjbQLSearchCriteria) criteria);
		}
		return null;
	}

	private SearchResult<T> find(EjbQLSearchCriteria criteria) {
		List<T> result = new ArrayList<T>();
		long resultSize = 0;

		// if no criteria are supplied, return nothing
		if (criteria == null || criteria.getQuery() == null) {
			return new SearchResult<T>(result, result.size(), 0, 0);
		}

		javax.persistence.Query query = getEntityManager().createQuery(
				criteria.getQuery());

		result = find(query, criteria.getPageSize(), criteria.getCurrentPage(),
				criteria.getParameters());
		if (criteria.getCountQuery() == null) {
			resultSize = result.size();
		} else {
			resultSize = count(getEntityManager().createQuery(
					criteria.getCountQuery()), criteria.getParameters());
		}
		return new SearchResult<T>(result, resultSize, criteria.getPageSize(),
				criteria.getCurrentPage());
	}

	private SearchResult<T> find(NamedQuerySearchCriteria criteria) {
		List<T> result = new ArrayList<T>();
		long resultSize = 0;

		// if no criteria are supplied, return nothing
		if (criteria == null || criteria.getQueryName() == null) {
			return new SearchResult<T>(result, result.size(), 0, 0);
		}
		javax.persistence.Query query = getEntityManager().createNamedQuery(
				criteria.getQueryName());
		result = find(query, criteria.getPageSize(), criteria.getCurrentPage(),
				criteria.getParameters());

		if (criteria.getCountQueryName() == null) {
			resultSize = result.size();
		} else {
			resultSize = count(getEntityManager().createNamedQuery(
					criteria.getCountQueryName()), criteria.getParameters());
		}
		return new SearchResult<T>(result, resultSize, criteria.getPageSize(),
				criteria.getCurrentPage());
	}

	@SuppressWarnings("unchecked")
	private List<T> find(javax.persistence.Query query, int pageSize,
			int currentPage, Object... parameters) {
		for (int i = 0; i < parameters.length; i++) {
			query.setParameter(i + 1, parameters[i]);
		}

		if (currentPage > 0) {
			query.setFirstResult(pageSize * currentPage);
		}
		if (pageSize > 0) {
			query.setMaxResults(pageSize);
		}
		final List<T> result = (List<T>) query.getResultList();
		firePostLoad(result);
		return result;
	}

	private Long count(javax.persistence.Query query, Object... parameters) {
		for (int i = 0; i < parameters.length; i++) {
			query.setParameter(i + 1, parameters[i]);
		}
		return (Long) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	private SearchResult<T> find(FullTextSearchCriteria criteria) {
		log.info("search with criteria: " + criteria);
		List<T> result = new ArrayList<T>();

		// if no criteria are supplied, return nothing
		if (criteria == null) {
			return new SearchResult<T>(result, result.size(), 0, 0);
		}

		final int firstResult = criteria.getPageSize()
				* criteria.getCurrentPage();
		final int maxResults = criteria.getPageSize();

		// if no search pattern or properties are specified, just find all
		// entities
		if (StringUtils.isEmpty(criteria.getSearchString())
				|| (criteria.getPropertiesToSearch() == null)
				|| (criteria.getPropertiesToSearch().size() == 0)) {
			return new SearchResult<T>(findAll(firstResult, maxResults,
					criteria.getSort()), countAll(), criteria.getPageSize(),
					criteria.getCurrentPage());
		}

		try {
			Session session = (Session) getEntityManager().getDelegate();
			FullTextSession fullTextSession = Search
					.createFullTextSession(session);
			MultiFieldQueryParser parser = new MultiFieldQueryParser(criteria
					.getArrayOfPropertiesToSearch(), new StandardAnalyzer());
			parser.setAllowLeadingWildcard(true);
			Query query = parser.parse(criteria.getSearchString());
			FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(
					query, persistentClass);

			// first get the total count
			final int totalCount = fullTextQuery.getResultSize();

			// now get the data for the current page
			if (firstResult > 0) {
				fullTextQuery.setFirstResult(firstResult);
			}

			if (maxResults > 0) {
				fullTextQuery.setMaxResults(maxResults);
			}

			addOrder(fullTextQuery, criteria.getSort());
			result = fullTextQuery.list();
			
			firePostLoad(result);

			return new SearchResult<T>(result, totalCount, criteria
					.getPageSize(), criteria.getCurrentPage());

		} catch (final ParseException ex) {
			return new SearchResult<T>(result, 0, 0, 0);
		}
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#makeTransient(java.io.Serializable)
	 */
	@Override
	public void makeTransient(ID id) {
		delete(id);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#delete(java.lang.Object)
	 */
	@Override
	public void delete(T entity) {
		firePreDelete(entity);
		getEntityManager().remove(entity);
		firePostDelete(entity);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#delete(java.io.Serializable)
	 */
	@Override
	public void delete(ID id) {
		T entity = findById(id);
		delete(entity);
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#save(java.lang.Object)
	 */
	@Override
	public T save(T entity) {
		firePreSave(entity);
		final T savedEntity = getEntityManager().merge(entity);
		firePostSave(savedEntity);
		return savedEntity;
	}
	
	public void setInterceptors(Collection<Interceptor<T>> interceptors) {
		this.interceptors.clear();
		if (interceptors != null && !interceptors.isEmpty()) {
			this.interceptors.addAll(interceptors);
		}
	}
	
	public Collection<Interceptor<T>> getInterceptors() {
		return interceptors;
	}
	
	protected void firePreSave(T entity) {
		if (interceptors.isEmpty()) return;
		for (Interceptor<T> interceptor : interceptors) {
			interceptor.preSave(entity);
		}
	}
	
	protected void firePostSave(T entity) {
		if (interceptors.isEmpty()) return;
		for (Interceptor<T> interceptor : interceptors) {
			interceptor.postSave(entity);
		}
	}
	
	protected void firePreDelete(T entity) {
		if (interceptors.isEmpty()) return;
		for (Interceptor<T> interceptor : interceptors) {
			interceptor.preDelete(entity);
		}
		
	}
	
	protected void firePostDelete(T entity) {
		if (interceptors.isEmpty()) return;
		for (Interceptor<T> interceptor : interceptors) {
			interceptor.postDelete(entity);
		}
	
	}
	
	protected void firePostLoad(T entity) {
		if (interceptors.isEmpty()) return;
		for (Interceptor<T> interceptor : interceptors) {
			interceptor.postLoad(entity);
		}
	}
	
	protected void firePostLoad(Collection<T> entities) {
		if (interceptors.isEmpty()) return;
		for (Interceptor<T> interceptor : interceptors) {
			interceptor.postLoad(entities);
		}
	}

	@Override
	public void iterateAll(GenericRepositoryCallback<T> callback) {
		iterateByCriteria(callback);
	}

	@Override
	public void iterateByNamedQuery(GenericRepositoryCallback<T> callback,
			String queryName, Object... params) {
		log.debug("iterate by named query: " + queryName);
		Session session = (Session) getEntityManager().getDelegate();
		org.hibernate.Query query = session.getNamedQuery(queryName);
		
		log.debug("found query " + query.getQueryString());
		
		for (String string : query.getNamedParameters()) {
			log.debug(string);
		}

		for (int i = 0; i < params.length; i++) {
			query.setParameter(String.valueOf(i+1), params[i]);
		}
		
		ScrollableResults scrollableResults = query.scroll(ScrollMode.FORWARD_ONLY);
		iterate(scrollableResults, session, callback);
	}
	
	protected void iterateByCriteria(GenericRepositoryCallback<T> callback, Criterion...criterion) {
		iterateByCriteria(callback, null, criterion);
	}
	
	protected void iterateByCriteria(GenericRepositoryCallback<T> callback, Sort sort, Criterion... criterion) {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());
		addOrder(crit, sort);

		for (final Criterion c : criterion) {
			crit.add(c);
		}

		ScrollableResults scrollableResults = crit.scroll(ScrollMode.FORWARD_ONLY);
		iterate(scrollableResults, session, callback);
	}
	
	@SuppressWarnings("unchecked")
	private void iterate(final ScrollableResults scrollableResults, final Session session, final GenericRepositoryCallback<T> callback) {
		while (scrollableResults.next()) {
			T entity = (T)scrollableResults.get(0);
			callback.process(entity);
			//remove the entity from the session, so we don't get memory trouble
			session.evict(entity);
		}
		
	}

	@Override
	public void iterateAll(GenericRepositoryCallback<T> callback, Sort sort) {
		iterateByCriteria(callback, sort);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		Session session = (Session) getEntityManager().getDelegate();
		Criteria crit = session.createCriteria(getEntityClass());

		ScrollableResults scrollableResults = crit.scroll(ScrollMode.FORWARD_ONLY);
		while (scrollableResults.next()) {
			T entity = (T)scrollableResults.get(0);
			delete(entity);
			session.flush();
		}
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository#flush()
	 */
	@Override
	public void flush() {
		Session session = (Session) getEntityManager().getDelegate();
		session.flush();
	}
}
