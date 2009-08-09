package be.bzbit.framework.i18n.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import be.bzbit.framework.domain.repository.Sort;
import be.bzbit.framework.domain.repository.search.SearchCriteria;
import be.bzbit.framework.domain.repository.search.SearchResult;
import be.bzbit.framework.exceptions.BusinessException;
import be.bzbit.framework.i18n.DatabaseMessageSource;
import be.bzbit.framework.i18n.dto.DictionaryEntry;
import be.bzbit.framework.i18n.dto.DictionaryLookup;
import be.bzbit.framework.i18n.dto.DictionaryPage;
import be.bzbit.framework.i18n.dto.Translation;
import be.bzbit.framework.i18n.exception.InvalidMessageException;
import be.bzbit.framework.i18n.model.Language;
import be.bzbit.framework.i18n.model.Message;
import be.bzbit.framework.i18n.repository.LanguageRepository;
import be.bzbit.framework.i18n.repository.MessageRepository;

public class DefaultTranslationServiceImpl implements Serializable,
		TranslationService {
	private static final Log log = LogFactory.getLog(DefaultTranslationServiceImpl.class);

	private LanguageRepository languageRepository;
	private MessageRepository messageRepository;
	private DatabaseMessageSource messageSource;

	public LanguageRepository getLanguageRepository() {
		return languageRepository;
	}

	@Required
	public void setLanguageRepository(LanguageRepository languageRepository) {
		this.languageRepository = languageRepository;
	}

	public MessageRepository getMessageRepository() {
		return messageRepository;
	}

	@Required
	public void setMessageRepository(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	private static final long serialVersionUID = -7520975674184188332L;

	@Override
	public void addMessage(String messageCode, String defaultTranslation) {
		if (StringUtils.isBlank(messageCode) || defaultTranslation == null)
			throw new BusinessException("ErrorInvalidMessage",
					new InvalidMessageException());
		Message message = new Message();
		message.setCode(messageCode);
		message.setMessage(defaultTranslation);
		getMessageRepository().save(message);
		fireMessageSourceChanged();
	}

	@Override
	public void removeMessage(String messageCode) {
		getMessageRepository().deleteMessages(messageCode);
		fireMessageSourceChanged();
	}

	@Override
	@Transactional(readOnly = true)
	public Translation getTranslation(String messageCode) {
		if (messageCode == null)
			return null;
		List<Message> messages = getMessageRepository()
				.getMessages(messageCode);
		List<Locale> locales = getAvailableLocales();
		return new Translation(messageCode, locales, messages);
	}

	@Override
	public void saveTranslation(Translation translation) {
		if (translation == null)
			return;
		Message defaultMessage = translation.getDefaultMessage();
		getMessageRepository().saveOrDelete(defaultMessage);
		for (Message message : translation.getMessages()) {
			getMessageRepository().saveOrDelete(message);
		}
		fireMessageSourceChanged();
	}

	@Override
	public void translate(String messageCode, Locale locale, String translation) {
		if (messageCode == null)
			throw new BusinessException("ErrorInvalidMessageCode", null);
		if (locale == null)
			throw new BusinessException("ErrorInvalidLocale", null);
		Message message = new Message();
		message.setCode(messageCode);
		message.setLocale(locale);
		message.setMessage(translation);
		getMessageRepository().saveOrDelete(message);
		fireMessageSourceChanged();
	}

	@Override
	public void addLocale(Locale locale) {
		Language existing = getLanguageRepository().getLanguage(locale);
		if (existing != null) return;
		Language language = new Language();
		language.setLocale(locale);
		getLanguageRepository().save(language);
		fireMessageSourceChanged();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Locale> getAvailableLocales() {
		List<Language> languages = getLanguageRepository().findAll();
		List<Locale> locales = new ArrayList<Locale>(languages.size());
		for (Language language : languages) {
			locales.add(language.getLocale());
		}
		return locales;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Message> getDefaultMessages() {
		return getMessageRepository().getMessages(null, null);
	}

	@Override
	public void removeLocale(Locale locale) {
		Language languageToRemove = getLanguageRepository().getLanguage(locale);
		if (languageToRemove == null)
			throw new BusinessException("LanguageNotFound", null);
		getLanguageRepository().delete(languageToRemove);
		getMessageRepository().deleteMessages(locale.getLanguage(), locale.getCountry());
		fireMessageSourceChanged();
	}

	@Override
	@Transactional(readOnly = true)
	public SearchResult<Language> findLanguages(SearchCriteria languageCriteria) {
		return getLanguageRepository().find(languageCriteria);
	}

	@Override
	@Transactional(readOnly = true)
	public SearchResult<Message> findMessages(SearchCriteria searchCriteria) {
		return getMessageRepository().find(searchCriteria);
	}

	private void fireMessageSourceChanged() {
		log.info("Firing MessageSource update");
		if (messageSource == null)
			return;
		messageSource.clearCache();
	}

	public DatabaseMessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(DatabaseMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	@Transactional(readOnly=true)
	public DictionaryPage getDictionaryPage(DictionaryLookup lookup) {
		final int firstResult = lookup.getPageSize() * lookup.getCurrentPage();
		final int maxResults = lookup.getPageSize();
		final Sort sort = new Sort("code");
		//first load the default messages
		final List<Message> defaultMessages = getMessageRepository().getMessages(null, null, firstResult, maxResults, sort);
		//now get the codes of these messages
		final List<String> messageCodes = new ArrayList<String>(defaultMessages.size());
		for (Message message : defaultMessages) {
			messageCodes.add(message.getCode());
		}
		//Now that we have the codes, fetch the translations
		final List<Message> translatedMessages = getMessageRepository().getMessages(lookup.getLocale().getLanguage(), lookup.getLocale().getCountry(), messageCodes, sort);
		final int totalNumberOfMessages = getMessageRepository().countMessages(null, null);
		final Map<String,DictionaryEntry> entryMap = new LinkedHashMap<String,DictionaryEntry>(maxResults);
		for (Message message : defaultMessages) {
			entryMap.put(message.getCode(), new DictionaryEntry(message));
		}
		for (Message message : translatedMessages) {
			final DictionaryEntry entry = entryMap.get(message.getCode());
			if (entry != null) entry.setTranslatedMessage(message);
		}
		List<DictionaryEntry> entries = new ArrayList<DictionaryEntry>(entryMap.size());
		entries.addAll(entryMap.values());
		final DictionaryPage result = new DictionaryPage(lookup.getLocale(), entries, totalNumberOfMessages, lookup.getPageSize(), lookup.getCurrentPage());
		log.debug(result);
		return result; 
	}

	@Override
	@Transactional
	public void saveDictionaryPage(DictionaryPage page) {
		log.debug("saving dictionary page: " + page);
		if (page == null) return;
		for (DictionaryEntry entry : page.getEntries()) {
			Message translatedMessage = getMessageRepository().getMessage(page.getLocale().getLanguage(), page.getLocale().getCountry(), entry.getMessageCode());
			if (translatedMessage == null) {
				translatedMessage = new Message(entry.getMessageCode());
				translatedMessage.setLocale(page.getLocale());
			}
			translatedMessage.setMessage(entry.getTranslatedMessage());
			getMessageRepository().saveOrDelete(translatedMessage);
		}
		fireMessageSourceChanged();
	}

}
