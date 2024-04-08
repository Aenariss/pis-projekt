/*
 * PIS Projekt 2024
 * OrderItem.java
 * @author Filip Brna <xbrnaf00>
 */
package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Data model for an order item.
 */
@Entity
@Table(name = "OrderItem")
@NamedQuery(name = "OrderItem.findAll", query = "SELECT oi FROM OrderItem oi")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Positive
    private int quantity;

    @ManyToMany
    @JoinTable(name = "OrderItemCategory", joinColumns = @JoinColumn(name = "order_item_id"), inverseJoinColumns = @JoinColumn(name = "order_id"))
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_item_id")
    private ProductItem productItem;

    public OrderItem() {
    }

    public OrderItem(int quantity, Order order, ProductItem productItem) {
        this.quantity = quantity;
        this.order = order;
        this.productItem = productItem;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }
}