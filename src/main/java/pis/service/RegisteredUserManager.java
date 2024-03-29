/**
 * PIS Projekt 2024
 * UserManager.java
 * @author Vojtech Fiala <xfiala61>
 */

package pis.service;
import java.util.List;

import pis.data.RegisteredUser;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.Query;

/**
 * Business logic for working with users.
 */
@RequestScoped
public class RegisteredUserManager {
    @PersistenceContext
    private EntityManager em;

    /**
     * Returns list of all users.
     */
    public List<RegisteredUser> findAll() {
        return em.createNamedQuery("RegisteredUser.findAll", RegisteredUser.class).getResultList();
    }

    /**
     * Returns users if they exist, otherwise null.
     * @param id Id of the searched users.
     */
    public RegisteredUser find(long id) {
        return em.find(RegisteredUser.class, id);
    }

    /**
     * Returns users if they exist, otherwise null.
     * @param email Email of the searched user.
     */
    public RegisteredUser findByEmail(String email) {
        RegisteredUser u = null;
        try {
            Query q = em.createQuery("SELECT u FROM RegisteredUser u WHERE u.email = :email");
            q.setParameter("email", email); // is this safe?
            return (RegisteredUser) q.getSingleResult();
        }
        catch(Exception e) {
            return u;
        }
    }

    /**
     * Add user to users.
     * @param c user to add.
     * @return Returns inserted user.
     */
    @Transactional
    public RegisteredUser save(RegisteredUser c) {
        return em.merge(c);
    }

    /**
     * Remove user from users
     * @param c user to remove
     * @return If succeeded or nots
     */
    @Transactional
    public void delete(RegisteredUser c) {
        RegisteredUser new_c = em.merge(c);
        em.remove(new_c);
    }
}
