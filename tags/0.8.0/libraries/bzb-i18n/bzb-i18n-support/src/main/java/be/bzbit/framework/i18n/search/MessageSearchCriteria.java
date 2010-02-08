package be.bzbit.framework.i18n.search;

import org.apache.commons.lang.StringUtils;

import be.bzbit.framework.domain.repository.search.NamedQuerySearchCriteria;

public class MessageSearchCriteria extends NamedQuerySearchCriteria {
	
	private static final long serialVersionUID = 4981551618997418273L;
	private String code;
	
	public MessageSearchCriteria() {
		super();
		setQueryName("findDefaultMessageByCode");
		setCountQueryName("countDefaultMessageByCode");
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	@Override
	public Object[] getParameters() {
		final String c = StringUtils.isNotBlank(code) ? "%" + code + "%" : "%";
		return new Object[]{c};
	}
}
