package be.bzbit.framework.domain.faces;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;

import be.bzbit.framework.domain.model.DomainObject;


/**
 * Convert a list of domain object in a list of JSF SelectItems
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 */
public class DomainModelSelectItemAdapter
        implements Serializable {
    //~ Static fields/initializers ---------------------------------------------

    private static final long serialVersionUID = -6660052783022205459L;

    //~ Methods ----------------------------------------------------------------

    public static <T extends DomainObject> List<SelectItem> getSelectItems(
        final Collection<?extends DomainObject> values
    ) {
        List<SelectItem> items = new ArrayList<SelectItem>();

        if (values == null) {
            return items;
        }

        for (final DomainObject value : values) {
            items.add(new SelectItem(value, value.getLabel()));
        }

        return items;
    }
}
