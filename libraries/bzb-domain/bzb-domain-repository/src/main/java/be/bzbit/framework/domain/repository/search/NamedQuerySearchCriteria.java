package be.bzbit.framework.domain.repository.search;

import java.io.Serializable;

/**
 * Search Criteria with support for paging, using named queries
 * 
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 * 
 * @version $LastChangedRevision: 228 $
 */
public class NamedQuerySearchCriteria extends AbstractSearchCriteria implements
		Serializable {

	private static final long serialVersionUID = 1923226028592205197L;
	
	private String queryName;
	private String countQueryName;
	private Object[] parameters;

	public NamedQuerySearchCriteria() {
		super();
	}
	
	public void setParameters(Object... params) {
		this.parameters = params;
	}

	public Object[] getParameters() {
		return parameters;
	}
	
	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getCountQueryName() {
		return countQueryName;
	}

	public void setCountQueryName(String countQueryName) {
		this.countQueryName = countQueryName;
	}


}
