package be.bzbit.framework.i18n.service;

import java.util.List;
import java.util.Locale;

import be.bzbit.framework.domain.repository.search.SearchCriteria;
import be.bzbit.framework.domain.repository.search.SearchResult;
import be.bzbit.framework.i18n.dto.DictionaryLookup;
import be.bzbit.framework.i18n.dto.DictionaryPage;
import be.bzbit.framework.i18n.dto.Translation;
import be.bzbit.framework.i18n.model.Language;
import be.bzbit.framework.i18n.model.Message;

public interface TranslationService {
	void translate(String messageCode, Locale locale, String translation);
	void addMessage(String messageCode, String defaultTranslation);
        void removeMessage(String messageCode);
	Translation getTranslation(String messageCode);
	void saveTranslation(Translation translation);
	DictionaryPage getDictionaryPage(DictionaryLookup lookup);
	void saveDictionaryPage(DictionaryPage page);
	void addLocale(Locale locale);
	void removeLocale(Locale locale);
	List<Locale> getAvailableLocales();
	List<Message> getDefaultMessages();
	SearchResult<Message> findMessages(SearchCriteria searchCriteria);
	SearchResult<Language> findLanguages(SearchCriteria languageCriteria);
}
