package be.bzbit.framework.i18n;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import be.bzbit.framework.i18n.model.Message;

public class DatabaseMessageSource extends AbstractMessageSource {
	private SimpleJdbcTemplate jdbcTemplate;
	
	@Required
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	private Map<Locale, Properties> cachedBundles = new HashMap<Locale, Properties>();
	
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		return createMessageFormat(loadMessage(code, locale), locale);
	}

	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		return loadMessage(code, locale);
	}

	protected String loadMessage(String code, Locale locale) {
		Properties bundle = loadBundle(locale);
		return bundle.getProperty(code);
	}
	
	protected Properties loadBundle(Locale locale) {
		synchronized (this.cachedBundles) {
			if (cachedBundles.containsKey(locale)) return cachedBundles.get(locale);
			Properties bundle = getMessagesFromDatabase(locale);
			cachedBundles.put(locale, bundle);
			return bundle;
		}
	}

	protected Properties getMessagesFromDatabase(Locale locale) {
		Properties bundle = new Properties();
		
		String sql = "select language_code, country_code, message_code, message from I18N_MESSAGE where ";

	    ParameterizedRowMapper<Message> mapper = new ParameterizedRowMapper<Message>() {
	    
	        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
	            Message message = new Message();
	            message.setCode(rs.getString("message_code"));
	            message.setMessage(rs.getString("message"));
	            message.setLanguage(rs.getString("language_code"));
	            message.setCountry(rs.getString("country_code"));
	            return message;
	        }
	    };

	    addToBundle(bundle, this.jdbcTemplate.query(sql + "language_code is null and country_code is null", mapper));
	    addToBundle(bundle, this.jdbcTemplate.query(sql + "language_code = ? and country_code is null", mapper, locale.getLanguage()));
	    addToBundle(bundle, this.jdbcTemplate.query(sql + "language_code = ? and country_code = ?", mapper, locale.getLanguage(), locale.getCountry()));
	    
	    return bundle;
	    
		
	}
	
	protected void addToBundle(Properties target, List<Message> source) {
		for (Message message : source) {
			target.put(message.getCode(), message.getMessage());
		}
	}
	
	public void clearCache() {
		synchronized (this.cachedBundles) {
			this.cachedBundles.clear();
		}
	}
	
}
