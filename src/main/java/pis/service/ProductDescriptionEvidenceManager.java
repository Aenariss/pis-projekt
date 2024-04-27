/*
 * PIS Projekt 2024
 * ProductDescriptionEvidenceManager.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.service;

import pis.data.ProductDescriptionEvidence;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Business logic for working with QuantityEvidence.
 */
@RequestScoped
public class ProductDescriptionEvidenceManager {

    @PersistenceContext
    private EntityManager em;

    public ProductDescriptionEvidence find(long id) {
        return em.find(ProductDescriptionEvidence.class, id);
    }

    @Transactional
    public ProductDescriptionEvidence save(ProductDescriptionEvidence quantityEvidence) {
        return em.merge(quantityEvidence);
    }
}