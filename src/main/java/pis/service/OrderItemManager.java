/*
 * PIS Projekt 2024
 * OrderItemManager.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.service;

import java.util.List;

import pis.data.OrderItem;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.Query;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Business logic for working with OrderItem.
 */
@RequestScoped
public class OrderItemManager {

    @PersistenceContext
    private EntityManager em;

    /**
     * Returns list of all OrderItem.
     */
    public List<OrderItem> findAll() {
        return em.createNamedQuery("OrderItem.findAll", OrderItem.class).getResultList();
    }

    public OrderItem find(long id) {
        return em.find(OrderItem.class, id);
    }

    public OrderItem findByProductDescriptionId(long productDescriptionId) {
        OrderItem orderItem = null;
        try {
            Query q = em.createQuery("SELECT o FROM OrderItem o WHERE o.productDescription.id = :productDescriptionId");
            q.setParameter("productDescriptionId", productDescriptionId);
            return (OrderItem) q.getSingleResult();
        } catch (Exception e) {
            System.out.println(e);
            return orderItem;
        }
    }

    @Transactional
    public OrderItem save(OrderItem orderItem) {
        return em.merge(orderItem);
    }

    @Transactional
    public void delete(OrderItem orderItem) {
        OrderItem new_orderItem = em.merge(orderItem);
        em.remove(new_orderItem);
    }
}