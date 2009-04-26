package be.bzbit.framework.faces.tags;

import be.bzbit.framework.faces.util.JsfFunctions;

import com.sun.facelets.tag.AbstractTagLibrary;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * Facelets Tag Library for some custom EL functions
 *
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 *
 * @version $LastChangedRevision: 172 $
 */
public class BzbTagLibrary
        extends AbstractTagLibrary {
    //~ Static fields/initializers ---------------------------------------------

    /** Namespace used to import this library in Facelets pages  */
    public static final String NAMESPACE = "http://www.bzb-it.be/bzb-faces/el";

    /**  Current instance of library. */
    public static final BzbTagLibrary INSTANCE = new BzbTagLibrary();

    //~ Constructors -----------------------------------------------------------

    public BzbTagLibrary() {
        super(NAMESPACE);

        try {
            Method[] methods = JsfFunctions.class.getMethods();

            for (int i = 0; i < methods.length; i++) {
                if (Modifier.isStatic(methods[i].getModifiers())) {
                    this.addFunction(methods[i].getName(), methods[i]);
                }
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
