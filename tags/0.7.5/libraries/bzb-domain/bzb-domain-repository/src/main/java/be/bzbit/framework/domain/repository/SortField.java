/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.bzbit.framework.domain.repository;

import org.apache.commons.lang.builder.ToStringBuilder;

import be.bzbit.framework.domain.repository.Sort.Direction;


/**
 * Only for use in the Sort class
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 */
public class SortField {
    //~ Instance fields --------------------------------------------------------

    private Direction direction;
    private String propertyName;

    //~ Constructors -----------------------------------------------------------

    public SortField(
        final Direction direction,
        final String propertyName
    ) {
        super();
        this.direction = direction;
        this.propertyName = propertyName;
    }

    //~ Methods ----------------------------------------------------------------

    public Direction getDirection() {
        return direction;
    }

    public String getPropertyName() {
        return propertyName;
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(propertyName).append(direction).toString();
	}
}
