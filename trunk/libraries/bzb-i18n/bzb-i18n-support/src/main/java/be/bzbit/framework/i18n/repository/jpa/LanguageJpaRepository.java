package be.bzbit.framework.i18n.repository.jpa;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import be.bzbit.framework.domain.repository.jpa.DomainObjectJpaRepository;
import be.bzbit.framework.i18n.model.Language;
import be.bzbit.framework.i18n.repository.LanguageRepository;

public class LanguageJpaRepository extends DomainObjectJpaRepository<Language>
		implements LanguageRepository {

	@Override
	public Language getLanguage(Locale locale) {
		if (locale == null) return null;
		Criterion languageCriterion = null;
		Criterion countryCriterion = null;
		if (StringUtils.isEmpty(locale.getLanguage())) {
			languageCriterion = Restrictions.isNull("languageCode");
		} else {
			languageCriterion = Restrictions.eq("languageCode", locale
					.getLanguage());
		}
		if (StringUtils.isEmpty(locale.getCountry())) {
			countryCriterion = Restrictions.isNull("countryCode");
		} else {
			countryCriterion = Restrictions.eq("countryCode", locale
					.getCountry());
		}
		List<Language> result = findByCriteria(languageCriterion,
				countryCriterion);
		if (result == null || result.isEmpty()) {
			return null;
		} else {
			return result.get(0);
		}
	}

}
