/**
 * PIS Projekt 2024
 * CategoryManager.java
 * @author Lukáš Petr <xpetrl06>
 * @author Vojtech Fiala <xfiala61>
 */

package pis.service;
import java.util.List;

import pis.data.Category;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Business logic for working with categories.
 */
@RequestScoped
public class CategoryManager {
    @PersistenceContext
    private EntityManager em;

    /**
     * Returns list of all categories.
     */
    public List<Category> findAll() {
        return em.createNamedQuery("Category.findAll", Category.class).getResultList();
    }

    /**
     * Returns category if it exists, otherwise null.
     * @param id Id of the searched category.
     */
    public Category find(long id) {
        return em.find(Category.class, id);
    }

    /**
     * Returns category by name if it exists, otherwise null.
     * @param name name of the searched category.
     */
    public Category findByName(String name) {
        Category cat = null;
        try {
            Query q = em.createQuery("SELECT c FROM Category c WHERE c.name = :name");
            q.setParameter("name", name); // is this safe?
            return (Category) q.getSingleResult();
        }
        catch(Exception e) {
            System.out.println(e);
            return cat;
        }
    }

    /**
     * Add category to categories.
     * @param c Category to add.
     * @return Returns inserted category.
     */
    @Transactional
    public Category save(Category c) {
        return em.merge(c);
    }

    /**
     * Remove category from categories
     * @param c Category to remove
     * @return If succeeded or nots
     */
    @Transactional
    public void delete(Category c) {
        Category new_c = em.merge(c);
        em.remove(new_c);
    }
}
