/**
 * PIS Projekt 2024
 * OrderManager.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.service;

import java.util.List;

import pis.data.Order;
import pis.data.OrderStatus;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Business logic for working with Orders.
 */
@RequestScoped
public class OrderManager {
    @PersistenceContext
    private EntityManager em;

    /**
     * Returns list of all Orders.
     */
    public List<Order> findAll() {
        return em.createNamedQuery("Order.findAll", Order.class).getResultList();
    }

    /**
     * Returns Order if it exists, otherwise null.
     * 
     * @param id Id of the searched Order.
     * @return Order with given id.
     */
    public Order find(long id) {
        return em.find(Order.class, id);
    }

    /**
     * Returns Orders by state if it exists, otherwise null.
     * 
     * @param o OrderState of the searched Order.
     * @return Order
     */
    @SuppressWarnings("unchecked")
    public List<Order> findByState(OrderStatus o) {
        List<Order> orders = null;
        try {
            Query q = em.createQuery("SELECT o FROM Order o WHERE o.state = :state");
            q.setParameter("state", o);
            return (List<Order>) q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return orders;
        }
    }

    /**
     * Returns Orders by date if it exists, otherwise null.
     * 
     * @param creationDate of the searched Order.
     * @return Order
     */
    public Order findByCreationDate(String creationDate) {
        Order order = null;
        try {
            Query q = em.createQuery("SELECT o FROM Order o WHERE o.creationDate = :creationDate");
            q.setParameter("creationDate", creationDate);
            return (Order) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return order;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Order> findByEmail(String email) {
        List<Order> orders = null;
        try {
            Query q = em.createQuery(
                    "SELECT o FROM Order o WHERE lower(o.orderUserInfo.email) = lower( :email )");
            q.setParameter("email", email);
            return (List<Order>) q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return orders;
        }
    }

    /**
     * Add Order to db.
     * 
     * @param o Order to add.
     * @return Returns inserted Order.
     */
    @Transactional
    public Order save(Order o) {
        return em.merge(o);
    }

}
