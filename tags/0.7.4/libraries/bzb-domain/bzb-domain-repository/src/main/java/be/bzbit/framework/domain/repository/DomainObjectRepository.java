/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package be.bzbit.framework.domain.repository;

import be.bzbit.framework.domain.model.DomainObject;

/**
 * A Generic Repository for DomainObjects
 *
 * @author Jurgen Lust
 * @author $LastChangedBy$
 *
 * @version $LastChangedRevision$
 *
 * @param <T> the entity type
 */
public interface DomainObjectRepository<T extends DomainObject>
        extends GenericRepository<T, Long> {}
