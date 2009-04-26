package be.bzbit.framework.i18n.repository.jpa;

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import be.bzbit.framework.domain.repository.Sort;
import be.bzbit.framework.domain.repository.jpa.DomainObjectJpaRepository;
import be.bzbit.framework.i18n.model.Message;
import be.bzbit.framework.i18n.repository.MessageRepository;

public class MessageJpaRepository extends DomainObjectJpaRepository<Message>
		implements MessageRepository {

	@Override
	public Properties loadMessages(Locale locale) {
		Criterion languageCriterion = Restrictions.eq("language", locale
				.getLanguage());
		Criterion countryCriterion = Restrictions.eq("country", locale
				.getCountry());
		Criterion nullLanguageCriterion = Restrictions.isNull("language");
		Criterion nullCountryCriterion = Restrictions.isNull("country");
		Properties bundle = new Properties();
		// first load default values, where neither language nor country are
		// specified
		addToBundle(bundle, findByCriteria(nullLanguageCriterion,
				nullCountryCriterion));
		// then overwrite with values where only language is specified
		addToBundle(bundle, findByCriteria(languageCriterion,
				nullCountryCriterion));
		// finally overwrite with values where both language and country are
		// specified
		addToBundle(bundle, findByCriteria(languageCriterion, countryCriterion));

		return bundle;
	}

	protected void addToBundle(Properties target, List<Message> source) {
		for (Message message : source) {
			target.put(message.getCode(), message.getMessage());
		}
	}

	@Override
	public List<Message> getMessages(String language, String country) {
		return getMessages(language, country, 0, 0, null);
	}

	@Override
	public List<Message> getMessages(String language, String country,
			int firstResult, int maxResults, Sort sort) {
		final Criterion languageCriterion = StringUtils.isBlank(language) ? Restrictions
				.isNull("language")
				: Restrictions.eq("language", language);
		final Criterion countryCriterion = StringUtils.isBlank(country) ? Restrictions
				.isNull("country")
				: Restrictions.eq("country", country);
		return findByCriteria(firstResult, maxResults, sort,
			languageCriterion, countryCriterion);
	}
	
	@Override
	public List<Message> getMessages(String language, String country,
			List<String> codes, Sort sort) {
		final Criterion languageCriterion = StringUtils.isBlank(language) ? Restrictions
				.isNull("language")
				: Restrictions.eq("language", language);
		final Criterion countryCriterion = StringUtils.isBlank(country) ? Restrictions
				.isNull("country")
				: Restrictions.eq("country", country);
		final Criterion codesCriterion = Restrictions.in("code", codes);
		return findByCriteria(sort, languageCriterion, countryCriterion, codesCriterion);
	}
	
	
	@Override
	public int countMessages(String language, String country) {
		final Criterion languageCriterion = StringUtils.isBlank(language) ? Restrictions
				.isNull("language")
				: Restrictions.eq("language", language);
		final Criterion countryCriterion = StringUtils.isBlank(country) ? Restrictions
				.isNull("country")
				: Restrictions.eq("country", country);
		return countByCriteria(languageCriterion, countryCriterion);
	}

	@Override
	public Message getMessage(String language, String country, String code) {
		final Criterion languageCriterion = StringUtils.isBlank(language) ? Restrictions
				.isNull("language")
				: Restrictions.eq("language", language);
		final Criterion countryCriterion = StringUtils.isBlank(country) ? Restrictions
				.isNull("country")
				: Restrictions.eq("country", country);
		final Criterion codeCriterion = Restrictions.eq("code", code);
		List<Message> messages = findByCriteria(languageCriterion,
				countryCriterion, codeCriterion);
		if (messages == null || messages.isEmpty())
			return null;
		return messages.get(0);
	}

	@Override
	public List<Message> getMessages(String messageCode) {
		Criterion codeCriterion = Restrictions.eq("code", messageCode);
		return findByCriteria(codeCriterion);
	}

	@Override
	public void saveOrDelete(Message message) {
		if (message == null)
			return;
		if (message.getId() == null) {
			// This message was not in the database yet
			if (StringUtils.isNotBlank(message.getCode())
					&& StringUtils.isNotBlank(message.getMessage())) {
				// only save the message if it has a code and a text
				save(message);
			}
		} else {
			// This message was already in the database
			if (StringUtils.isNotBlank(message.getCode())
					&& StringUtils.isNotBlank(message.getMessage())) {
				// only save the message if it has a code and a text
				save(message);
			} else {
				// otherwise delete it
				delete(message.getId());
			}
		}

	}

	@Override
	public void deleteMessages(String messageCode) {
		if (messageCode == null)
			return;
		List<Message> messages = getMessages(messageCode);
		for (Message message : messages) {
			delete(message);
		}
	}

	@Override
	public void deleteMessages(String language, String country) {
		List<Message> messages = getMessages(language, country);
		for (Message message : messages) {
			delete(message);
		}
	}

	@Override
	public Message save(Message entity) {
		Message savedEntity = getMessage(entity.getLanguage(), entity.getCountry(), entity.getCode());
		if (savedEntity == null) {
			getEntityManager().persist(entity);
			return entity;
		} else {
			savedEntity.setCode(entity.getCode());
			savedEntity.setCountry(entity.getCountry());
			savedEntity.setLanguage(entity.getLanguage());
			savedEntity.setMessage(entity.getMessage());
			return getEntityManager().merge(savedEntity);
		}
	}

	
	
}
