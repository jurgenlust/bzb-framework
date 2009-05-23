package be.bzbit.framework.domain.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.search.annotations.DocumentId;


/**
 * Superclass for persistent domain objects, also providing PropertyChangeSupport,
 * which may be useful for binding frameworks.
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 */
@MappedSuperclass
public abstract class DomainObject
        implements Identifiable, Validatable, Displayable, Serializable {
    //~ Instance fields --------------------------------------------------------

	private static final long serialVersionUID = 4288323831260059972L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @DocumentId
    private Long id;
    @Version
    @XmlTransient
    private Long version;
    @Transient
    private final PropertyChangeSupport propertyChangeSupport;

    //~ Constructors -----------------------------------------------------------

    public DomainObject() {
        super();
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * @return the business identity
     */
    public final Long getId() {
        return id;
    }
    
    /**
     * setter for id should only be used for testing purposes
     * 
     * @param id the business identity
     */
    public final void setId(Long id) {
    	this.id = id;
    }

    /**
     * @see org.edoframework.domainmodel.Identifiable#getIdentity()
     */
    public final Object getIdentity() {
        return id;
    }

    /**
     * @return the version, used by Hibernate for optimistic locking
     */
    protected final Long getVersion() {
        return version;
    }

    protected final PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    public final void addPropertyChangeListener(
        final PropertyChangeListener listener
    ) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public final void removePropertyChangeListener(
        final PropertyChangeListener listener
    ) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = (PRIME * result) + ((id == null) ? 0 : id.hashCode());

        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final DomainObject other = (DomainObject) obj;

        if (id == null) {
            return false;
        } else if (!id.equals(other.id)) {
            return false;
        }

        return true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                                        .append("version", version)
                                        .append("label", getLabel()).toString();
    }

	/**
	 * @see be.bzbit.framework.domain.model.Validatable#isValid()
	 */
	@Override
	public Boolean isValid() {
		return true;
	}

    
}
