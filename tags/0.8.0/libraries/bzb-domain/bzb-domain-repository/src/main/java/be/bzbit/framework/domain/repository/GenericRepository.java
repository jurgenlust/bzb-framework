package be.bzbit.framework.domain.repository;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import be.bzbit.framework.domain.repository.search.SearchCriteria;
import be.bzbit.framework.domain.repository.search.SearchResult;


/**
 * Generic Repository, providing basic CRUD operations
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 *
 * @param <T> the entity type
 * @param <ID> the primary key type
 */
public interface GenericRepository<T, ID extends Serializable> {
    //~ Methods ----------------------------------------------------------------

    /**
     * Get the Class of the entity
     *
     * @return the class
     */
    Class<T> getEntityClass();

    /**
     * Find an entity by its primary key
     *
     * @param id the primary key
     * @return the entity
     */
    T findById(final ID id);

    /**
     * Find an entity by its primary key, optionally locking the record in
     * the database
     *
     * @param id the primary key
     * @param lock should the record be locked?
     * @return the entity
     */
    T findById(
        final ID id,
        final boolean lock
    );
    
    
    /**
     * Load all entities
     *
     * @return the list of entities
     */
    List<T> findAll();

    /**
     * Load all entities, sorted as specified
     *
     * @param sort the sorting
     *
     * @return the sorted list of entities
     */
    List<T> findall(final Sort sort);

    /**
     * Load the specified range from all entities
     *
     * @param firstResult the first row to load
     * @param maxResults the number of rows to load
     *
     * @return the list of entities
     */
    List<T> findAll(
        final int firstResult,
        final int maxResults
    );

    /**
     * Load the specified range from all entities, sorted
     *
     * @param firstResult the first row to load
     * @param maxResults the number of rows to load
     * @param sort the sorting
     *
     * @return the list of entities
     */
    List<T> findAll(
        final int firstResult,
        final int maxResults,
        final Sort sort
    );

    /**
     * Find entities based on an example
     *
     * @param exampleInstance the example
     * @return the list of entities
     */
    List<T> findByExample(final T exampleInstance);

    /**
     * Find entities based on an example
     *
     * @param exampleInstance the example
     * @param sort the sorting
     * @return the list of entities
     */
    List<T> findByExample(
        final T exampleInstance,
        final Sort sort
    );

    /**
     * Find the specified range of entities based on an example
     *
     * @param exampleInstance the example
     * @param firstResult the first row to load
     * @param maxResults the number of rows to load
     *
     * @return the list of entities
     */
    List<T> findByExample(
        final T exampleInstance,
        final int firstResult,
        final int maxResults
    );

    /**
     * Find the specified range of entities based on an example
     *
     * @param exampleInstance the example
     * @param firstResult the first row to load
     * @param maxResults the number of rows to load
     * @param sort the sorting
     *
     * @return the list of entities
     */
    List<T> findByExample(
        final T exampleInstance,
        final int firstResult,
        final int maxResults,
        final Sort sort
    );

    /**
     * Find using a named query
     *
     * @param queryName the name of the query
     * @param params the query parameters
     *
     * @return the list of entities
     */
    List<T> findByNamedQuery(
        final String queryName,
        Object... params
    );

    /**
     * Find using a named query
     *
     * @param queryName the name of the query
     * @param params the query parameters
     *
     * @return the list of entities
     */
    List<T> findByNamedQueryAndNamedParams(
        final String queryName,
        final Map<String, ?extends Object> params
    );

    /**
     * Find using SearchCriteria, with support for paging and sorting
     *
     * @param criteria the search criteria
     * @return the search result, including the total number of results
     */
    SearchResult<T> find(SearchCriteria criteria);
    
    /**
     * Count all entities
     *
     * @return the number of entities
     */
    int countAll();

    /**
     * Count entities based on an example
     *
     * @param exampleInstance the search criteria
     * @return the number of entities
     */
    int countByExample(final T exampleInstance);

    /**
     * save an entity
     * 
     * @deprecated use save(final T entity)
     *
     * @param entity the entity to save
     * @return the entity
     */
    T makePersistent(final T entity);
    
    /**
     * save an entity. This can be either a INSERT or UPDATE in the database
     * 
     * @param entity the entity to save
     * 
     * @return the saved entity
     */
    T save(final T entity);

    /**
     * delete an entity
     * 
     * @deprecated use remove(final T entity); 
     *
     * @param entity the entity to delete
     */
    void makeTransient(final T entity);
    
    /**
     * delete an entity from the database
     * 
     * @param entity the entity to delete
     */
    void delete(final T entity);
    
    /**
     * delete an entity
     * 
     * @deprecated use remove(final T entity);
     * 
     * @param id the primary key of the entity
     */
    void makeTransient(final ID id);
    
    /**
     * delete an entity
     * 
     * @param id the primary key of the entity
     */
    void delete(final ID id);
    
    /**
     * iterate all entities. This is a readonly operation!
     * 
     * @param callback the callback that is used for processing the entity
     */
    void iterateAll(GenericRepositoryCallback<T> callback);

    /**
     * iterate all entities. This is a readonly operation!
     * 
     * @param callback the callback that is used for processing the entity
     */
    void iterateAll(GenericRepositoryCallback<T> callback, Sort sort);
    
    /**
     * iterate entities. This is a readonly operation!
     * 
     * @param callback the callback that is used for processing the entity
     * @param queryName the name of the NamedQuery
     * @param params the parameters of the NamedQuery
     */
    void iterateByNamedQuery(GenericRepositoryCallback<T> callback, String queryName, Object... params);
    
    /**
     * delete all entities.
     */
    void clear();
    
    /**
     * flush the pending operations to the database
     */
    void flush();
}
