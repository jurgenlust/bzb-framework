package be.bzbit.framework.domain.repository.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 * Search Criteria with support for paging, using full text search
 * 
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 * 
 * @version $LastChangedRevision: 223 $
 */
public class FullTextSearchCriteria extends AbstractSearchCriteria implements Serializable {
	private static final long serialVersionUID = -1705792818141695841L;
	private String searchString;
	final List<String> propertiesToSearch;

	public FullTextSearchCriteria() {
		super();
		this.propertiesToSearch = new ArrayList<String>();
	}

	public FullTextSearchCriteria(String searchString) {
		this();
		this.searchString = searchString;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public List<String> getPropertiesToSearch() {
		return propertiesToSearch;
	}

	public String[] getArrayOfPropertiesToSearch() {
		String[] result = new String[propertiesToSearch.size()];
		return propertiesToSearch.toArray(result);
	}

	public void setPropertiesToSearch(List<String> propertiesToSearch) {
		this.propertiesToSearch.clear();
		if (propertiesToSearch != null) {
			this.propertiesToSearch.addAll(propertiesToSearch);
		}
	}

	public void setPropertiesToSearch(String[] propertiesToSearch) {
		this.propertiesToSearch.clear();
		if (propertiesToSearch != null) {
			for (String string : propertiesToSearch) {
				this.propertiesToSearch.add(string);
			}
		}
	}

}
