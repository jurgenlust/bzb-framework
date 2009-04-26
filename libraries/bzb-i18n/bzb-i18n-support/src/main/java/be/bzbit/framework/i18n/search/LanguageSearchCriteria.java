package be.bzbit.framework.i18n.search;

import org.apache.commons.lang.StringUtils;

import be.bzbit.framework.domain.repository.search.NamedQuerySearchCriteria;

public class LanguageSearchCriteria extends NamedQuerySearchCriteria {
	private static final long serialVersionUID = 1434721432950056170L;
	private String code;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public LanguageSearchCriteria() {
		super();
		setQueryName("findLanguageByCodes");
		setCountQueryName("countLanguageByCodes");
	}
	

	@Override
	public Object[] getParameters() {
		final String c = StringUtils.isNotBlank(code) ? code : "%";
		
		return new Object[]{c};
	}
}
