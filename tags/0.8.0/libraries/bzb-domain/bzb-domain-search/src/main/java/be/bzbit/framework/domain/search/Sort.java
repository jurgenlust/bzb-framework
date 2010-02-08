/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.bzbit.framework.domain.search;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import be.bzbit.framework.domain.search.SortField.Direction;


/**
 * Use this class to specify sorting when performing queries
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 */
public class Sort
        implements Serializable {
    //~ Static fields/initializers ---------------------------------------------

    private static final long serialVersionUID = 1187769662336922682L;

    //~ Instance fields --------------------------------------------------------

    private final List<SortField> fields;

    //~ Constructors -----------------------------------------------------------

    public Sort(
        final Direction direction,
        final String... propertyNames
    ) {
        this();

        for (final String propertyName : propertyNames) {
            this.fields.add(new SortField(direction, propertyName));
        }
    }

    public Sort() {
        super();
        this.fields = new ArrayList<SortField>();
    }

    public Sort(String... propertyNames) {
        this();

        for (final String propertyName : propertyNames) {
            this.fields.add(new SortField(Direction.ASC, propertyName));
        }
    }

    //~ Methods ----------------------------------------------------------------

    public List<SortField> getFields() {
        return fields;
    }
    
    public void setSortProperties(String... propertyNames) {
    	reset();
    	
        for (final String propertyName : propertyNames) {
            this.fields.add(new SortField(Direction.ASC, propertyName));
        }
    }
    
    public void setSortProperties(Direction direction, String... propertyNames) {
    	reset();
    	
        for (final String propertyName : propertyNames) {
            this.fields.add(new SortField(direction, propertyName));
        }
    }
    
    public void setSingleProperty(String propertyName, boolean isAscending) {
    	reset();
    	this.fields.add(new SortField(isAscending ? Direction.ASC : Direction.DESC, propertyName));
    }
    
    public void reset() {
    	this.fields.clear();
    }

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(fields).toString();
	}


}
