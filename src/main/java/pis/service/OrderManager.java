/**
 * PIS Projekt 2024
 * OrderManager.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.service;

import java.time.LocalDateTime;
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

    /*****************************/
    /** STATISTICS CALCULATIONS **/
    /*****************************/

    /**
     * Return the number of order made between dates
     * 
     * @param from from Beginning of the range (including)
     * @param to from End of the range (including)
     * @return List of pairs of times & order counts
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> salesTimeRange(LocalDateTime from, LocalDateTime to) {
        List<Object[]> results = null;
        try {
            // FUNCTION -- to convert full dateTime to YYYY-MM-DD, which is good for visualization
            Query query = em.createQuery("SELECT FUNCTION('DATE', o.creationDate), COUNT(o) FROM Order o WHERE o.creationDate BETWEEN :from AND :to GROUP BY FUNCTION('DATE', o.creationDate)");
            query.setParameter("from", from);
            query.setParameter("to", to);
            return (List<Object[]>) query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return results;
        }
    }

    /**
     * Return the number of order per category made between dates
     * 
     * @param from from Beginning of the range (including)
     * @param to from End of the range (including)
     * @return List of pairs of cateogires & order counts
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> categoriesSalesInRange(LocalDateTime from, LocalDateTime to) {
        List<Object[]> results = null;
        try {
            // maybe after we improve the data per order
            //Query query = em.createQuery("SELECT c.name AS category, COUNT(o) FROM Order o JOIN o.orderItems oi JOIN oi.categories c WHERE o.creationDate BETWEEN :from AND :to GROUP BY c.name");
            Query query = em.createQuery("SELECT c.name, COUNT(o) FROM Order o JOIN o.orderItems oi JOIN oi.productDescription pd JOIN pd.categories c WHERE o.creationDate BETWEEN :from AND :to GROUP BY c.name");
            query.setParameter("from", from);
            query.setParameter("to", to);
            return (List<Object[]>) query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return results;
        }
    }

    /**
     * Return the number of order per category made between dates
     * 
     * @param from from Beginning of the range (including)
     * @param to from End of the range (including)
     * @return List of pairs of items & order counts
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> itemSalesInRange(LocalDateTime from, LocalDateTime to) {
        List<Object[]> results = null;
        try {
            // maybe after we improve the data per order
            //Query query = em.createQuery("SELECT oi.name, COUNT(o) FROM Order o JOIN o.orderItems oi JOIN WHERE o.creationDate BETWEEN :from AND :to GROUP BY c.name");
            Query query = em.createQuery("SELECT pd.name, COUNT(o) FROM Order o JOIN o.orderItems oi JOIN oi.productDescription pd WHERE o.creationDate BETWEEN :from AND :to GROUP BY pd.name");
            query.setParameter("from", from);
            query.setParameter("to", to);
            query.setMaxResults(20); // Limit the results to the top 20 books
            return (List<Object[]>) query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return results;
        }
    }

    /**
     * Return the earnings per day in given time range
     * 
     * @param from from Beginning of the range (including)
     * @param to from End of the range (including)
     * @return List of pairs of days & corresponding earnings
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> earningsInRange(LocalDateTime from, LocalDateTime to) {
        List<Object[]> results = null;
        try {
            // maybe after we improve the data per order
            //Query query = em.createQuery("SELECT oi.name, COUNT(o) FROM Order o JOIN o.orderItems oi JOIN WHERE o.creationDate BETWEEN :from AND :to GROUP BY c.name");
            Query query = em.createQuery("SELECT FUNCTION('DATE', o.creationDate), SUM(o.totalPrice) FROM Order o WHERE o.creationDate BETWEEN :from AND :to GROUP BY FUNCTION('DATE', o.creationDate)");
            query.setParameter("from", from);
            query.setParameter("to", to);
            return (List<Object[]>) query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return results;
        }
    }

}
