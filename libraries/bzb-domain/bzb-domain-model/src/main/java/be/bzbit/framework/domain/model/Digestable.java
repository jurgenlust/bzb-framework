package be.bzbit.framework.domain.model;

import java.util.SortedMap;

/**
 * A Digestable entity contains a column with the message digest of the record
 *
 * @author jlust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 */
public interface Digestable {
    void setDigest(String digest);

    String getDigest();

    SortedMap<String,String> getDigestInput();
    
    void setDigestValid(boolean valid);
    
    boolean isDigestValid();
}
