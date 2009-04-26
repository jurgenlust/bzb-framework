package be.bzbit.framework.faces;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.lang.reflect.ParameterizedType;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;


/**
 * Adapter for displaying comboboxes using enums
 *
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 *
 * @version $LastChangedRevision: 173 $
 *
 * @param <T> The enum type
 */
public class EnumSelectItemAdapter<T extends Enum<T>> {
    //~ Instance fields --------------------------------------------------------

    private final Class<T> enumClass;
    private List<SelectItem> items = null;
    private MessageSource messageSource;

    //~ Constructors -----------------------------------------------------------

    @SuppressWarnings("unchecked")
    public EnumSelectItemAdapter() {
        super();
        this.enumClass = (Class<T>) ((ParameterizedType) getClass()
                                                             .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public EnumSelectItemAdapter(final Class<T> enumClass) {
        super();
        this.enumClass = enumClass;
    }

    //~ Methods ----------------------------------------------------------------

    public List<SelectItem> getSelectItems() {
        if (items == null) {
            T[] constants = enumClass.getEnumConstants();
            items = new ArrayList<SelectItem>();

            for (final T constant : constants) {
                SelectItem item =
                    new SelectItem(
                        constant.name(), getMessage(constant.name())
                    );
                items.add(item);
            }
        }

        return items;
    }

    private String getMessage(final String enumValue) {
        if (getMessageSource() == null) {
            return enumValue;
        }

        try {
            return getMessageSource()
                       .getMessage(
                enumValue, null,
                FacesContext.getCurrentInstance().getViewRoot().getLocale()
            );
        } catch (final NoSuchMessageException e) {
            return enumValue;
        }
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
