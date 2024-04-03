/**
 * PIS Projekt 2024
 * BookAuthorManager.java
 * @author Tomas Ondrusek <xondru18>
 */

package pis.service;

import java.util.List;

import pis.data.BookAuthor;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Business logic for working with BookAuthors.
 */
@RequestScoped
public class BookAuthorManager {
    @PersistenceContext
    private EntityManager em;

    /**
     * Returns list of all BookAuthors.
     */
    public List<BookAuthor> findAll() {
        return em.createNamedQuery("BookAuthor.findAll", BookAuthor.class).getResultList();
    }

    /**
     * Returns BookAuthor if it exists, otherwise null.
     * 
     * @param id Id of the searched BookAuthor.
     * @return BookAuthor with given id.
     */
    public BookAuthor find(long id) {
        return em.find(BookAuthor.class, id);
    }

    /**
     * Returns BookAuthor by name if it exists, otherwise null.
     * 
     * @param name name of the searched BookAuthor.
     * @return BookAuthor
     */
    public BookAuthor findByLastName(String lastName) {
        BookAuthor author = null;
        try {
            Query q = em.createQuery("SELECT a FROM BookAuthor a WHERE a.lastName = :lastName");
            q.setParameter("lastName", lastName);
            return (BookAuthor) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return author;
        }
    }

    /**
     * Returns BookAuthor by first and last name if it exists, otherwise null.
     * 
     * @param firstName first name of the searched BookAuthor.
     * @param lastName  last name of the searched BookAuthor.
     * @return BookAuthor
     */
    public BookAuthor findByName(String firstName, String lastName) {
        BookAuthor author = null;
        try {
            Query q = em.createQuery(
                    "SELECT a FROM BookAuthor a WHERE a.firstName = :firstName AND a.lastName = :lastName");
            q.setParameter("firstName", firstName);
            q.setParameter("lastName", lastName);
            return (BookAuthor) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return author;
        }
    }

    /**
     * Add BookAuthor to db.
     * 
     * @param a BookAuthor to add.
     * @return Returns inserted BookAuthor.
     */
    @Transactional
    public BookAuthor save(BookAuthor a) {
        return em.merge(a);
    }

    /**
     * Remove BookAuthor from db.
     * 
     * @param a BookAuthor to remove
     * @return If succeeded or not
     */
    @Transactional
    public void delete(BookAuthor a) {
        BookAuthor new_a = em.merge(a);
        em.remove(new_a);
    }
}
