/*
 * PIS Projekt 2024
 * Order.java
 * @author Filip Brna <xbrnaf00>
 */
package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data model for an order.
 */
@Entity
@Table(name = "Orders")
@NamedQuery(name = "Order.findAll", query = "SELECT o FROM Order o")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderState state;

    @NotNull
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "order_user_info_id")
    private OrderUserInfo orderUserInfo;

    @Embedded
    private Address deliveryAddress;

    @Embedded
    private Address userAddress;

    public Order() {
    }

    public Order(OrderState state, LocalDateTime creationDate, OrderUserInfo orderUserInfo, List<OrderItem> orderItems,
            Address deliveryAddress, Address userAddress) {
        this.state = state;
        this.creationDate = creationDate;
        this.orderUserInfo = orderUserInfo;
        this.deliveryAddress = deliveryAddress;
        this.userAddress = userAddress;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public OrderUserInfo getOrderUserInfo() {
        return orderUserInfo;
    }

    public void setOrderUserInfo(OrderUserInfo orderUserInfo) {
        this.orderUserInfo = orderUserInfo;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Address getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(Address userAddress) {
        this.userAddress = userAddress;
    }

}