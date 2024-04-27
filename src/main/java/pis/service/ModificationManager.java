/*
 * PIS Projekt 2024
 * ModificationManager.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.service;

import pis.data.Modification;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Business logic for working with OrderItem.
 */
@RequestScoped
public class ModificationManager {

    @PersistenceContext
    private EntityManager em;

    public Modification find(long id) {
        return em.find(Modification.class, id);
    }

    @Transactional
    public Modification save(Modification modification) {
        return em.merge(modification);
    }
}