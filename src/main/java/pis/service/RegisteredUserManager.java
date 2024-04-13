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
            System.out.println(e);
            return u;
        }
    }

    /**
     * Returns list of all users who are also employees.
     */
    @SuppressWarnings("unchecked")
    public List<RegisteredUser> findEmployees() {
        List<RegisteredUser> employees = null;
        try {
            Query q = em.createQuery("SELECT u FROM RegisteredUser u WHERE (u.role = 'employee' OR u.role = 'admin')");
            return (List<RegisteredUser>) q.getResultList();
        }
        catch (Exception e) {
            System.out.println(e);
            return employees;
        } 
    }

    /**
     * Returns list of all employees who match the given name.
     */
    @SuppressWarnings("unchecked")
    public List<RegisteredUser> getEmployeeQuery(String name) {
        List<RegisteredUser> employees = null;
        try {
            Query q = em.createQuery("SELECT u FROM RegisteredUser u WHERE (u.role = 'employee' OR u.role = 'admin') AND u.surname LIKE :name");
            q.setParameter("name", '%' + name + '%');
            return (List<RegisteredUser>) q.getResultList();
        }
        catch (Exception e) {
            System.out.println(e);
            return employees;
        } 
        
    }

    /**
     * Returns list of all employees who match the given email.
     */
    @SuppressWarnings("unchecked")
    public List<RegisteredUser> getEmployeeByEmailQuery(String email) {
        List<RegisteredUser> employees = null;
        try {
            Query q = em.createQuery("SELECT u FROM RegisteredUser u WHERE (u.role = 'employee' OR u.role = 'admin') AND u.email LIKE :email");
            q.setParameter("email", '%' + email + '%');
            return (List<RegisteredUser>) q.getResultList();
        }
        catch (Exception e) {
            System.out.println(e);
            return employees;
        } 
    }

    /**
     * Returns list of all users who match the given name.
     */
    @SuppressWarnings("unchecked")
    public List<RegisteredUser> getUserQuery(String name) {
        List<RegisteredUser> users = null;
        try {
            Query q = em.createQuery("SELECT u FROM RegisteredUser u WHERE u.surname LIKE :name");
            q.setParameter("name", '%' + name + '%');
            return (List<RegisteredUser>) q.getResultList();
        }
        catch (Exception e) {
            System.out.println(e);
            return users;
        } 
    }

    /**
     * Returns list of all users who match the given email.
     */
    @SuppressWarnings("unchecked")
    public List<RegisteredUser> getUserByEmailQuery(String email) {
        List<RegisteredUser> users = null;
        try {
            Query q = em.createQuery("SELECT u FROM RegisteredUser u WHERE u.email LIKE :email");
            q.setParameter("email", '%' + email + '%');
            return (List<RegisteredUser>) q.getResultList();
        }
        catch (Exception e) {
            System.out.println(e);
            return users;
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
