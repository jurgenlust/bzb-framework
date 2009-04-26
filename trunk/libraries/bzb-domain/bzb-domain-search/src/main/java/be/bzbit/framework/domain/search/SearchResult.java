package be.bzbit.framework.domain.search;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Data Transfer Object for search results, including the total number of
 * results were found
 *  
 * @author jlust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 *
 * @param <T> the type of persistent object
 */
public class SearchResult<T> implements Serializable {
	private static final long serialVersionUID = 2485940878200997744L;
	private final List<T> result;
	private final long totalNumberOfResults;
	private final int currentPage;
	private final int pageSize;

	public SearchResult(List<T> result, long totalNumberOfResults, int pageSize, int currentPage) {
		super();
		this.result = result;
		this.totalNumberOfResults = totalNumberOfResults;
		this.pageSize = pageSize;
		this.currentPage = currentPage;
	}

	public List<T> getList() {
		return Collections.unmodifiableList(result);
	}

	public long getTotalNumberOfResults() {
		return totalNumberOfResults;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}
	
	public int getTotalNumberOfPages() {
		if (pageSize <= 0) return 1;
		int extraPage = totalNumberOfResults % pageSize == 0 ? 0 : 1; 
		return (int)(totalNumberOfResults/pageSize + extraPage);
	}
	
	public boolean isFirstPage() {
		return currentPage == 0;
	}
	
	public boolean isLastPage() {
		return currentPage == getTotalNumberOfPages() - 1; 
	}
 	
}
