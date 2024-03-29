package be.bzbit.framework.faces.util;

import org.apache.commons.lang.StringUtils;


/**
 * Some custom EL functions
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 */
public class JsfFunctions {
    //~ Methods ----------------------------------------------------------------

    public static String[] split(final String csv) {
        if (csv == null) {
            return null;
        }

        return StringUtils.split(csv, ',');
    }
}
