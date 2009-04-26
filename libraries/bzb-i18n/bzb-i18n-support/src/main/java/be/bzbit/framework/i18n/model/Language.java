package be.bzbit.framework.i18n.model;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import be.bzbit.framework.domain.model.DomainObject;

@Entity
@Table(name = "I18N_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"LANGUAGE_CODE", "COUNTRY_CODE" }) })
@NamedQueries( {
		@NamedQuery(name = "findLanguageByCodes", query = "Select l from Language l where l.languageCode like ?1 or l.countryCode like ?1 order by languageCode"),
		@NamedQuery(name = "countLanguageByCodes", query = "Select count(*) from Language l where l.languageCode like ?1 or l.countryCode like ?1") })
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Language extends DomainObject {

	private static final long serialVersionUID = 4916783337942111389L;

	@NotNull
	@Length(min = 2, max = 2)
	@Column(name = "LANGUAGE_CODE", length = 2)
	private String languageCode;
	@Length(max = 2)
	@Column(name = "COUNTRY_CODE", length = 2)
	private String countryCode;

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		if (languageCode == null || languageCode.trim().length() == 0) {
			this.languageCode = null;
		} else {
			this.languageCode = languageCode.toLowerCase();
		}
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		if (countryCode == null || countryCode.trim().length() == 0) {
			this.countryCode = null;
		} else {
			this.countryCode = countryCode.toUpperCase();
		}
	}

	public void setLocale(Locale locale) {
		setLanguageCode(locale.getLanguage());
		setCountryCode(locale.getCountry());
	}

	public Locale getLocale() {
		String l = languageCode == null ? "" : languageCode;
		String c = countryCode == null ? "" : countryCode;
		return new Locale(l, c);
	}
	
	public String getCode() {
		StringBuilder builder = new StringBuilder();
		if (languageCode != null) builder.append(languageCode);
		if (languageCode != null && countryCode != null) builder.append("_");
		if (countryCode != null) builder.append(countryCode);
		return builder.toString();
	}

	/**
	 * @see be.bzbit.framework.domain.model.Displayable#getLabel()
	 */
	@Override
	public String getLabel() {
		return getLocale().getDisplayName(getLocale());
	}

	/**
	 * @see be.bzbit.framework.domain.model.Displayable#getLabel(java.util.Locale)
	 */
	@Override
	public String getLabel(Locale locale) {
		return getLocale().getDisplayName(locale);
	}
}
