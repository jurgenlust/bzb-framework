package be.bzbit.framework.domain.model;

import org.hibernate.validator.AssertTrue;

/**
 * Provides a validation method for more complex validations than the standard
 * Hibernate annotations. 
 *
 * @author Jurgen Lust
 * @author $LastChangedBy: jlust $
 *
 * @version $LastChangedRevision: 202 $
 */
public interface Validatable {
	@AssertTrue Boolean isValid();
}
