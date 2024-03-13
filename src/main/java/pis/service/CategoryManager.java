package pis.service;
import java.util.List;

import pis.data.Category;
import jakarta.enterprise.context.RequestScoped;
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
     * Add category to categories.
     * @param c Category to add.
     * @return Returns inserted category.
     */
    @Transactional
    public Category save(Category c) {
        return em.merge(c);
    }
}
