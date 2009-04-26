package be.bzbit.framework.faces.converter;

import java.util.TimeZone;

/**
 * Custom DateTimeConverter that uses the server's timezone instead of GMT by
 * default.
 * 
 * @author jlust
 *
 */
public class DateTimeConverter extends javax.faces.convert.DateTimeConverter {

	public DateTimeConverter() {
		super();
		setTimeZone(TimeZone.getDefault());
	}

}
