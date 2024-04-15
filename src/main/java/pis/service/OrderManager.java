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
    public Order findByState(OrderStatus o) {
        Order order = null;
        try {
            Query q = em.createQuery("SELECT o FROM Order o WHERE o.state = :state");
            q.setParameter("state", o);
            return (Order) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return order;
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
    public List<Order> findByOrderUserInfo(String firstname, String surname, String phone, String email) {
        List<Order> orders = null;
        try {
            Query q = em.createQuery(
                    "SELECT o FROM Order o WHERE o.orderUserInfo.firstname = :firstname AND o.orderUserInfo.surname = :surname AND o.orderUserInfo.phone = :phone AND o.orderUserInfo.email = :email");
            q.setParameter("firstname", firstname);
            q.setParameter("surname", surname);
            q.setParameter("phone", phone);
            q.setParameter("email", email);
            return (List<Order>) q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return orders;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Order> findByOrderUserId(long id) {
        List<Order> orders = null;
        try {
            Query q = em.createQuery("SELECT o FROM Order o WHERE o.orderUserInfo.id = :id");
            q.setParameter("id", id);
            return (List<Order>) q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return orders;
        }
    }

    public List<Order> findByUserAddress(String state, String town, String street, Integer streetNumber,
            String postCode) {
        List<Order> orders = null;
        try {
            Query q = em.createQuery(
                    "SELECT o FROM Order o WHERE o.userAddress.state = :state AND o.userAddress.town = :town AND o.userAddress.street = :street AND o.userAddress.streetNumber = :streetNumber AND o.userAddress.postCode = :postCode");
            q.setParameter("state", state);
            q.setParameter("town", town);
            q.setParameter("street", street);
            q.setParameter("streetNumber", streetNumber);
            q.setParameter("postCode", postCode);
            return (List<Order>) q.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return orders;
        }
    }

    public List<Order> findByDeliveryAddress(String state, String town, String street, Integer streetNumber,
            String postCode) {
        List<Order> orders = null;
        try {
            Query q = em.createQuery(
                    "SELECT o FROM Order o WHERE o.deliveryAddress.state = :state AND o.deliveryAddress.town = :town AND o.deliveryAddress.street = :street AND o.deliveryAddress.streetNumber = :streetNumber AND o.deliveryAddress.postCode = :postCode");
            q.setParameter("state", state);
            q.setParameter("town", town);
            q.setParameter("street", street);
            q.setParameter("streetNumber", streetNumber);
            q.setParameter("postCode", postCode);
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

    /**
     * Remove Order from db.
     * 
     * @param o Order to remove
     * @return If succeeded or not
     */
    @Transactional
    public void delete(Order o) {
        Order new_o = em.merge(o);
        em.remove(new_o);
    }

    /**
     * Delete all Orders.
     * 
     * @return If succeeded or not
     */
    @Transactional
    public void deleteAll() {
        Query q = em.createQuery("DELETE FROM Order");
        q.executeUpdate();
    }
}
