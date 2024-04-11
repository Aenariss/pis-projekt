/*
 * PIS Projekt 2024
 * OrderUserInfoManager.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.service;

import java.util.List;

import pis.data.OrderUserInfo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Business logic for working with OrderUserInfo.
 */
@RequestScoped
public class OrderUserInfoManager {

    @PersistenceContext
    private EntityManager em;

    /**
     * Returns list of all OrderUserInfo.
     */
    public List<OrderUserInfo> findAll() {
        return em.createNamedQuery("OrderUserInfo.findAll", OrderUserInfo.class).getResultList();
    }

    public OrderUserInfo find(long id) {
        return em.find(OrderUserInfo.class, id);
    }

    public OrderUserInfo findBySurname(String surname) {
        OrderUserInfo orderUserInfo = null;
        try {
            Query q = em.createQuery("SELECT o FROM OrderUserInfo o WHERE o.surname = :surname");
            q.setParameter("surname", surname);
            return (OrderUserInfo) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return orderUserInfo;
        }
    }

    public OrderUserInfo findByFirstname(String firstname) {
        OrderUserInfo orderUserInfo = null;
        try {
            Query q = em.createQuery("SELECT o FROM OrderUserInfo o WHERE o.firstname = :firstname");
            q.setParameter("firstname", firstname);
            return (OrderUserInfo) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return orderUserInfo;
        }
    }

    public OrderUserInfo findByName(String firstname, String surname) {
        OrderUserInfo orderUserInfo = null;
        try {
            Query q = em.createQuery(
                    "SELECT o FROM OrderUserInfo o WHERE o.firstname = :firstname AND o.surname = :surname");
            q.setParameter("firstname", firstname);
            q.setParameter("surname", surname);
            return (OrderUserInfo) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return orderUserInfo;
        }
    }

    public OrderUserInfo findByPhone(String phone) {
        OrderUserInfo orderUserInfo = null;
        try {
            Query q = em.createQuery("SELECT o FROM OrderUserInfo o WHERE o.phone = :phone");
            q.setParameter("phone", phone);
            return (OrderUserInfo) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return orderUserInfo;
        }
    }

    public OrderUserInfo findByEmail(String email) {
        OrderUserInfo orderUserInfo = null;
        try {
            Query q = em.createQuery("SELECT o FROM OrderUserInfo o WHERE o.email = :email");
            q.setParameter("email", email);
            return (OrderUserInfo) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return orderUserInfo;
        }
    }

    @Transactional
    public OrderUserInfo save(OrderUserInfo o) {
        return em.merge(o);
    }

    @Transactional
    public void delete(OrderUserInfo o) {
        OrderUserInfo new_o = em.merge(o);
        em.remove(new_o);
    }

}