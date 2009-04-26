package be.bzbit.framework.domain.search;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Search Criteria with support for paging
 * 
 * @author Jurgen Lust
 * @author $LastChangedBy$
 * 
 * @version $LastChangedRevision$
 */
public class AbstractSearchCriteria implements SearchCriteria, Serializable {

	private static final long serialVersionUID = 400936247223103147L;
	protected int pageSize;
	protected int currentPage;
	private Sort sort;

	public AbstractSearchCriteria() {
		super();
		this.pageSize = 10;
		this.currentPage = 0;
	}

	/**
	 * @see be.bzbit.framework.domain.search.repository.search.SearchCriteria#getPageSize()
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @see be.bzbit.framework.domain.search.repository.search.SearchCriteria#setPageSize(int)
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @see be.bzbit.framework.domain.search.repository.search.SearchCriteria#getCurrentPage()
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @see be.bzbit.framework.domain.search.repository.search.SearchCriteria#setCurrentPage(int)
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @see be.bzbit.framework.domain.search.repository.search.SearchCriteria#getSort()
	 */
	public Sort getSort() {
		return sort;
	}

	/**
	 * @see be.bzbit.framework.domain.search.repository.search.SearchCriteria#setSort(be.bzbit.framework.domain.search.repository.Sort)
	 */
	public void setSort(Sort sort) {
		this.sort = sort;
	}

	/**
	 * @see be.bzbit.framework.domain.search.repository.search.SearchCriteria#nextPage()
	 */
	public void nextPage() {
		currentPage++;
	}

	/**
	 * @see be.bzbit.framework.domain.search.repository.search.SearchCriteria#previousPage()
	 */
	public void previousPage() {
		if (currentPage > 0)
			currentPage--;
	}

	/**
	 * @see be.bzbit.framework.domain.search.repository.search.SearchCriteria#resetPage()
	 */
	public void resetPage() {
		currentPage = 0;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(pageSize).append(currentPage)
				.append(sort).toString();
	}

}