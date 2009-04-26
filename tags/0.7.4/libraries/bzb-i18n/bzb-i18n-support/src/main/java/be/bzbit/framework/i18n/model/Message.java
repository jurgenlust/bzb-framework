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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import be.bzbit.framework.domain.model.DomainObject;

@Entity
@Table(name = "I18N_MESSAGE", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"MESSAGE_CODE", "LANGUAGE_CODE", "COUNTRY_CODE" }) })
@NamedQueries( {
		@NamedQuery(name = "findDefaultMessageByCode", query = "Select m from Message m where m.code like ?1 and language is null and country is null order by code"),
		@NamedQuery(name = "countDefaultMessageByCode", query = "Select count(*) from Message m where m.code like ?1 and language is null and country is null") })
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Message extends DomainObject {
	private static final long serialVersionUID = 6886751037607437511L;
	@Column(name = "MESSAGE_CODE", length = 100)
	@NotNull
	@Length(max = 100)
	@Index(name = "IDX_I18N_MESSAGE_CODE")
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		if (language == null || language.trim().length() == 0) {
			this.language = null;
		} else {
			this.language = language.toLowerCase();
		}
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if (country == null || country.trim().length() == 0) {
			this.country = null;
		} else {
			this.country = country.toUpperCase();
		}
	}

	public void setLocale(Locale locale) {
		if (locale == null) {
			setLanguage(null);
			setCountry(null);
		} else {
			setLanguage(locale.getLanguage());
			setCountry(locale.getCountry());
		}
	}

	@Column(name = "MESSAGE", length = 4000)
	@Length(max = 4000)
	@NotNull
	private String message;
	@Length(max = 2)
	@Column(name = "LANGUAGE_CODE", length = 2)
	@Index(name = "IDX_I18N_MESSAGE_LANGUAGE")
	private String language;
	@Length(max = 2)
	@Column(name = "COUNTRY_CODE", length = 2)
	@Index(name = "IDX_I18N_MESSAGE_COUNTRY")
	private String country;

	@Override
	public String getLabel() {
		return message;
	}

	@Override
	public String getLabel(Locale locale) {
		return message;
	}
	
	public String getLanguageLabel() {
		return getLocale().getDisplayName(getLocale());
	}
	
	public String getLanguageLabel(Locale locale) {
		return getLocale().getDisplayName(locale);
	}

	public String getLocaleCode() {
		if (language == null && country == null)
			return null;
		StringBuilder builder = new StringBuilder();
		if (language != null)
			builder.append(language);
		if (language != null && country != null)
			builder.append("_");
		if (country != null)
			builder.append(country);
		return builder.toString();
	}

	public Locale getLocale() {
		return new Locale(language == null ? "" : language,
				country == null ? "" : country);
	}

	public Message() {
		super();
	}

	public Message(String code) {
		super();
		this.code = code;
	}

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append(
            "language", language
        ).append("country", country).append("message", message).toString();
    }
}
