package be.bzbit.framework.faces;

import com.sun.facelets.impl.DefaultResourceResolver;
import com.sun.facelets.impl.ResourceResolver;

import org.apache.log4j.Logger;

import java.net.URL;


/**
 * A Facelets ResourceResolver that allows looking in the classpath for
 * Facelet templates
 *
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 *
 * @version $LastChangedRevision: 172 $
 */
public class ClasspathResourceResolver
        extends DefaultResourceResolver
        implements ResourceResolver {
    //~ Static fields/initializers ---------------------------------------------

    private static final Logger logger =
        Logger.getLogger(ClasspathResourceResolver.class);

    //~ Methods ----------------------------------------------------------------

    public URL resolveUrl(final String path) {
        if (logger.isDebugEnabled()) {
            logger.debug("Resolving resource: " + path);
        }

        URL url = super.resolveUrl(path);

        if (url == null) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "Resource not found in webapp, checking classpath"
                );
            }

            String myPath = path;

            if (myPath.startsWith("/")) {
                myPath = myPath.substring(1);
            }

            url = Thread.currentThread().getContextClassLoader()
                        .getResource(myPath);
        }

        if (url == null) {
            logger.warn("Resource not found in classpath: " + path);
        }

        return url;
    }
}