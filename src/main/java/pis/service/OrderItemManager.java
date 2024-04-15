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

    public OrderItem find(long id) {
        return em.find(OrderItem.class, id);
    }

    @Transactional
    public OrderItem save(OrderItem orderItem) {
        return em.merge(orderItem);
    }
}