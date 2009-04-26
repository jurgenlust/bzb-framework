package be.bzbit.framework.i18n.repository;

import java.util.Locale;

import be.bzbit.framework.domain.repository.DomainObjectRepository;
import be.bzbit.framework.i18n.model.Language;

public interface LanguageRepository extends DomainObjectRepository<Language> {
	Language getLanguage(Locale locale);
}
