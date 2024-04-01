/**
 * PIS Projekt 2024
 * DiscountManager.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.service;
import java.util.List;

import pis.data.Discount;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Business logic for working with Discounts.
 */
@RequestScoped
public class DiscountManager {
    @PersistenceContext
    private EntityManager em;

    /**
     * Returns list of all Languages.
     */
    public List<Discount> findAll() {
        return em.createNamedQuery("Discount.findAll", Discount.class).getResultList();
    }

    /**
     * Returns Discount if it exists, otherwise null.
     * @param id Id of the searched Discount.
     */
    public Discount find(long id) {
        return em.find(Discount.class, id);
    }

    /**
     * Returns Discount by name if it exists, otherwise null.
     * @param name name of the searched Discount.
     */
    public Discount findDiscount(int discount) {
        Discount d = null;
        try {
            Query q = em.createQuery("SELECT d FROM Discount d WHERE d.discount = :discount");
            q.setParameter("discount", discount);
            return (Discount) q.getSingleResult();
        }
        catch(Exception e) {
            System.out.println(e);
            return d;
        }
    }

    /**
     * Add Discount to db.
     * @param d Discount to add.
     * @return Returns inserted Discount.
     */
    @Transactional
    public Discount save(Discount d) {
        return em.merge(d);
    }

    /**
     * Remove Discount from db.
     * @param d Discount to remove
     * @return If succeeded or not
     */
    @Transactional
    public void delete(Discount d) {
        Discount new_d = em.merge(d);
        em.remove(new_d);
    }
}
