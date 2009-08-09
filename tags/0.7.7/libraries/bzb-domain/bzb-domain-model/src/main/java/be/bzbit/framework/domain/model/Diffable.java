package be.bzbit.framework.domain.model;

import java.util.SortedMap;

/**
 * A Diffable entity offers a convenient method for generating diffs
 *
 * @author jlust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 */
public interface Diffable {
	SortedMap<String,String> getDiffInput();
}
