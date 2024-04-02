/**
 * PIS Projekt 2024
 * ProductDescriptionManager.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.service;
import java.util.List;

import pis.data.ProductDescription;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Business logic for working with ProductDescriptions.
 */
@RequestScoped
public class ProductDescriptionManager {
    @PersistenceContext
    private EntityManager em;

    /**
     * Returns list of all BookAuthors.
     */
    public List<ProductDescription> findAll() {
        return em.createNamedQuery("ProductDescription.findAll", ProductDescription.class).getResultList();
    }

    /**
     * Returns ProductDescription if it exists, otherwise null.
     * @param id Id of the searched BookAuthor.
     * @return ProductDescription with given id.
     */
    public ProductDescription find(long id) {
        return em.find(ProductDescription.class, id);
    }

    /**
     * Returns ProductDescription by name if it exists, otherwise null.
     * @param name name of the searched Product.
     * @return ProductDescription
     */
    public ProductDescription findProductDescription(String name) {
        ProductDescription product = null;
        try {
            Query q = em.createQuery("SELECT p FROM ProductDescription p WHERE p.name = :name");
            q.setParameter("name", name);
            return (ProductDescription) q.getSingleResult();
        }
        catch(Exception e) {
            System.out.println(e);
            return product;
        }
    }

    /**
     * Returns ProductDescriptions by author first and last name if it exists, otherwise null.
     * @param firstName first name of the searched Product.
     * @param lastName last name of the searched Product.
     * @return List of ProductDescriptions
     */
    public List<ProductDescription> findByAuthor(String firstName, String lastName) {
        List<ProductDescription> products = null;
        try {
            Query q = em.createQuery("SELECT p FROM ProductDescription p WHERE p.author.firstName = :firstName AND p.author.lastName = :lastName");
            q.setParameter("firstName", firstName);
            q.setParameter("lastName", lastName);
            return (List<ProductDescription>) q.getResultList();
        }
        catch(Exception e) {
            System.out.println(e);
            return products;
        }
    }

    /**
     * Returns ProductDescriptions by language if it exists, otherwise null.
     * @param language language of the searched Product.
     * @return List of ProductDescriptions
     */
    public List<ProductDescription> findByLanguage(String language) {
        List<ProductDescription> products = null;
        try {
            Query q = em.createQuery("SELECT p FROM ProductDescription p WHERE p.language.language = :language");
            q.setParameter("language", language);
            return (List<ProductDescription>) q.getResultList();
        }
        catch(Exception e) {
            System.out.println(e);
            return products;
        }
    }

    /**
     * Returns ProductDescriptions by category if it exists, otherwise null.
     * @param category category of the searched Product.
     * @return List of ProductDescriptions
     */
    public List<ProductDescription> findByCategory(String category) {
        List<ProductDescription> products = null;
        try {
            Query q = em.createQuery("SELECT p FROM ProductDescription p JOIN p.categories c WHERE c.name = :category OR c.description = :category");
            q.setParameter("category", category);
            return (List<ProductDescription>) q.getResultList();
        }
        catch(Exception e) {
            System.out.println(e);
            return products;
        }
    }

    /**
     * Returns ProductDescriptions by discount if it exists, otherwise null.
     * @param discount discount of the searched Product.
     * @return List of ProductDescriptions
     */
    public List<ProductDescription> findByDiscount(int discount) {
        List<ProductDescription> products = null;
        try {
            Query q = em.createQuery("SELECT p FROM ProductDescription p WHERE p.discount.discount = :discount");
            q.setParameter("discount", discount);
            return (List<ProductDescription>) q.getResultList();
        }
        catch(Exception e) {
            System.out.println(e);
            return products;
        }
    }

    /**
     * Returns ProductDescriptions by query if it exists, otherwise null.
     * @param query query of the searched Product.
     * Can be name, description, ISBN, author first and last name, category name and description, language.
     * @return List of ProductDescriptions
     */
    public List<ProductDescription> searchProductDescriptions(String query){
        List<ProductDescription> products = null;
        System.out.println(query);
        try {
            Query q = em.createQuery("SELECT p FROM ProductDescription p JOIN p.author a JOIN p.categories c WHERE p.name LIKE :query OR p.description LIKE :query OR p.ISBN LIKE :query OR a.firstName LIKE :query OR a.lastName LIKE :query OR c.name LIKE :query OR c.description LIKE :query OR p.language.language LIKE :query");
            System.out.println(query);
            q.setParameter("query", "%" + query + "%");
            
            return (List<ProductDescription>) q.getResultList();
        }
        catch(Exception e) {
            System.out.println(e);
            return products;
        }
    }

    /**
     * Add BookAuthor to db.
     * @param a BookAuthor to add.
     * @return Returns inserted BookAuthor.
     */
    @Transactional
    public ProductDescription save(ProductDescription d) {
        return em.merge(d);
    }

    /**
     * Remove BookAuthor from db.
     * @param a BookAuthor to remove
     * @return If succeeded or not
     */
    @Transactional
    public void delete(ProductDescription d) {
        ProductDescription new_d = em.merge(d);
        em.remove(new_d);
    }
}
