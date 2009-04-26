package be.bzbit.framework.domain.model;

import java.util.SortedMap;

/**
 * A Digestable entity contains a column with the message digest of the record
 *
 * @author jlust
 * @author $LastChangedBy: jlust $
 *
 * @version $LastChangedRevision: 317 $
 */
public interface Digestable {
    void setDigest(String digest);

    String getDigest();

    SortedMap<String,String> getDigestInput();
}
