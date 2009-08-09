package be.bzbit.framework.domain.persistence.search;

import java.io.Serializable;
import java.util.Set;

import be.bzbit.framework.domain.search.SearchCriteria;

public class SearchCriteriaHandlerFactory implements Serializable {
	private Set<SearchCriteriaHandler> searchCriteriaHandlers;

	public Set<SearchCriteriaHandler> getSearchCriteriaHandlers() {
		return searchCriteriaHandlers;
	}

	public void setSearchCriteriaHandlers(
			Set<SearchCriteriaHandler> searchCriteriaHandlers) {
		this.searchCriteriaHandlers = searchCriteriaHandlers;
	} 
	
	public SearchCriteriaHandler getHandler(SearchCriteria searchCriteria) {
		
	}
}
