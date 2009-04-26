package be.bzbit.framework.domain.repository.search;

import java.io.Serializable;

/**
 * Search Criteria with support for paging, using EJBQL queries
 * 
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 * 
 * @version $LastChangedRevision: 228 $
 */
public class EjbQLSearchCriteria extends AbstractSearchCriteria implements
		Serializable {
	private static final long serialVersionUID = -8033229519054994017L;
	private String query;
	private String countQuery;
	private Object[] parameters;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object... parameters) {
		this.parameters = parameters;
	}

	public void setCountQuery(String countQuery) {
		this.countQuery = countQuery;
	}

	public String getCountQuery() {
		return countQuery;
	}
}
