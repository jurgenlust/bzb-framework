package be.bzbit.framework.i18n.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import be.bzbit.framework.domain.repository.search.SearchResult;

public class DictionaryPage extends SearchResult<DictionaryEntry> implements
		Serializable {
	
	private final Locale locale;

	public DictionaryPage(Locale locale, List<DictionaryEntry> result,
			long totalNumberOfResults, int pageSize, int currentPage) {
		super(result, totalNumberOfResults, pageSize, currentPage);
		this.locale = locale;
	}

	private static final long serialVersionUID = -8792497851286176684L;
	
	public DictionaryEntry[] getEntries() {
		final List<DictionaryEntry> list = getList();
		return list.toArray(new DictionaryEntry[list.size()]);
	}

	public Locale getLocale() {
		return locale;
	}
}
