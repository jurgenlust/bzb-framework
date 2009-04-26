package be.bzbit.framework.domain.model;

import java.util.Locale;

/**
 * The label of a displayable object is used to provide
 * a human-readable textual representation of the object in the user
 * interface, for example in a combobox.
 *
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 *
 * @version $LastChangedRevision: 212 $
 */
public interface Displayable {
	String getLabel();
	String getLabel(Locale locale);
}
