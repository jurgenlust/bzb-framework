package be.bzbit.framework.i18n.dto;

import java.util.Locale;

import be.bzbit.framework.domain.repository.search.AbstractSearchCriteria;

public class DictionaryLookup extends AbstractSearchCriteria {
	private static final long serialVersionUID = 3762872791835316913L;
	
	private Locale locale;

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
