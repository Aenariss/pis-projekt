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
import pis.api.FilterQuery;
import pis.data.Category;
import pis.data.Language;

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
     * 
     * @param id Id of the searched BookAuthor.
     * @return ProductDescription with given id.
     */
    public ProductDescription find(long id) {
        return em.find(ProductDescription.class, id);
    }

    /**
     * Returns ProductDescription by name if it exists, otherwise null.
     * 
     * @param name name of the searched Product.
     * @return ProductDescription
     */
    public ProductDescription findProductDescription(String name) {
        ProductDescription product = null;
        try {
            Query q = em.createQuery("SELECT p FROM ProductDescription p WHERE p.name = :name");
            q.setParameter("name", name);
            return (ProductDescription) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return product;
        }
    }

    /**
     * Returns ProductDescriptions by author first and last name if it exists,
     * otherwise null.
     * 
     * @param firstName first name of the searched Product.
     * @param lastName  last name of the searched Product.
     * @return List of ProductDescriptions
     */
    public List<ProductDescription> findByAuthor(String firstName, String lastName) {
        List<ProductDescription> products = null;
        try {
            Query q = em.createQuery(
                    "SELECT p FROM ProductDescription p WHERE p.author.firstName = :firstName AND p.author.lastName = :lastName");
            q.setParameter("firstName", firstName);
            q.setParameter("lastName", lastName);
            return (List<ProductDescription>) q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return products;
        }
    }

    /**
     * Returns ProductDescriptions by language if it exists, otherwise null.
     * 
     * @param language language of the searched Product.
     * @return List of ProductDescriptions
     */
    public List<ProductDescription> findByLanguage(String language) {
        List<ProductDescription> products = null;
        try {
            Query q = em.createQuery("SELECT p FROM ProductDescription p WHERE p.language.language = :language");
            q.setParameter("language", language);
            return (List<ProductDescription>) q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return products;
        }
    }

    /**
     * Returns ProductDescriptions by category if it exists, otherwise null.
     * 
     * @param category category of the searched Product.
     * @return List of ProductDescriptions
     */
    public List<ProductDescription> findByCategory(String category) {
        List<ProductDescription> products = null;
        try {
            Query q = em.createQuery(
                    "SELECT p FROM ProductDescription p JOIN p.categories c WHERE c.name = :category OR c.description = :category");
            q.setParameter("category", category);
            return (List<ProductDescription>) q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return products;
        }
    }

    /**
     * Returns ProductDescriptions by discount if it exists, otherwise null.
     * 
     * @param discount discount of the searched Product.
     * @return List of ProductDescriptions
     */
    public List<ProductDescription> findByDiscount(int discount) {
        List<ProductDescription> products = null;
        try {
            Query q = em.createQuery("SELECT p FROM ProductDescription p WHERE p.discount.discount = :discount");
            q.setParameter("discount", discount);
            return (List<ProductDescription>) q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return products;
        }
    }

    /**
     * Returns ProductDescriptions by query if it exists, otherwise null.
     * 
     * @param query query of the searched Product.
     *              Can be name, description, ISBN, author first and last name,
     *              category name and description, language.
     * @return List of ProductDescriptions
     */
    public List<ProductDescription> searchProductDescriptions(String query) {
        List<ProductDescription> products = null;
        System.out.println(query);
        try {
            Query q = em.createQuery(
                    "SELECT p FROM ProductDescription p JOIN p.author a JOIN p.categories c WHERE p.name LIKE :query OR p.description LIKE :query OR p.ISBN LIKE :query OR a.firstName LIKE :query OR a.lastName LIKE :query OR c.name LIKE :query OR c.description LIKE :query OR p.language.language LIKE :query");
            System.out.println(query);
            q.setParameter("query", "%" + query + "%");

            return (List<ProductDescription>) q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return products;
        }
    }

    /**
     * Returns ProductDescriptions by filter query if it exists, otherwise null.
     * 
     * @param query query of the searched Product.
     *              {
     *              "authorIds": [id],
     *              "languageIds": [id],
     *              "categoryIds": [id],
     *              "priceFrom": double,
     *              "priceTo": double,
     *              "pagesFrom": int,
     *              "pagesTo": int,
     *              "discountFrom": int,
     *              "discountTo": int
     *              }
     * @return List of ProductDescriptions
     */
    @SuppressWarnings("unchecked")
    public List<ProductDescription> filterProductDescriptions(FilterQuery query) {

        List<ProductDescription> products = null;
        class FilterQueryData {
            private List<Long> authorIds;
            private List<Long> categoryIds;
            private List<Long> languageIds;
            private double priceFrom;
            private double priceTo;
            private int pagesFrom;
            private int pagesTo;
            private int discountFrom;
            private int discountTo;

            public void print() {
                System.out.println("Author IDs: " + authorIds);
                System.out.println("Category IDs: " + categoryIds);
                System.out.println("Language IDs: " + languageIds);
                System.out.println("Price From: " + priceFrom);
                System.out.println("Price To: " + priceTo);
                System.out.println("Pages From: " + pagesFrom);
                System.out.println("Pages To: " + pagesTo);
                System.out.println("Discount From: " + discountFrom);
                System.out.println("Discount To: " + discountTo);

            }
        }
        FilterQueryData data = new FilterQueryData();
        data.authorIds = query.getAuthorIds();
        data.categoryIds = query.getCategoryIds();
        data.languageIds = query.getLanguageIds();

        if (query.getPriceFrom() == null) {
            data.priceFrom = 0;
        } else {
            data.priceFrom = query.getPriceFrom();
        }
        if (query.getPriceTo() == null) {
            data.priceTo = 1000000;
        } else {
            data.priceTo = query.getPriceTo();
        }

        data.pagesFrom = query.getPagesFrom();
        data.pagesTo = query.getPagesTo();
        data.discountFrom = query.getDiscountFrom();
        data.discountTo = query.getDiscountTo();

        data.print(); // TODO remove debug print

        if (data.authorIds != null) {
            try {
                Query q = em.createQuery("SELECT p FROM ProductDescription p JOIN p.author a WHERE a.id IN :authorIds");
                q.setParameter("authorIds", data.authorIds);
                products = (List<ProductDescription>) q.getResultList();
            } catch (Exception e) {
                System.out.println(e);
                return products;
            }
        }

        // Filter products by category
        if (products != null && data.categoryIds != null) {
            products.removeIf(p -> {
                for (Category c : p.getCategories()) {
                    if (data.categoryIds.contains(c.getId())) {
                        return false;
                    }
                }
                return true;
            });
        }

        // Filter products by language
        if (products != null && data.languageIds != null) {
            for (ProductDescription p : products) {
                boolean found = false;
                Language l = p.getLanguage();
                for (Long id : data.languageIds) {
                    if (l.getId() == id) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    if (products != null) {
                        products.remove(p);
                    }
                    if (products.isEmpty()) {
                        return List.of();
                    }
                }
            }
        }
        // Filter products by price, pages and discount
        if (products != null) {
            products.removeIf(p -> p.getPrice() < data.priceFrom || p.getPrice() > data.priceTo);
        }
        if (products != null) {
            products.removeIf(p -> p.getPages() < data.pagesFrom || p.getPages() > data.pagesTo);
        }
        if (products != null) {
            products.removeIf(p -> p.getDiscount() != null && (p.getDiscount().getDiscount() < data.discountFrom
                    || p.getDiscount().getDiscount() > data.discountTo));
        }
        return products;
    }

    /**
     * Add BookAuthor to db.
     * 
     * @param a BookAuthor to add.
     * @return Returns inserted BookAuthor.
     */
    @Transactional
    public ProductDescription save(ProductDescription d) {
        return em.merge(d);
    }

    /**
     * Remove BookAuthor from db.
     * 
     * @param a BookAuthor to remove
     * @return If succeeded or not
     */
    @Transactional
    public void delete(ProductDescription d) {
        ProductDescription new_d = em.merge(d);
        em.remove(new_d);
    }
}
