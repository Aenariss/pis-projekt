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
    private OrderStatus status;

    @NotNull
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "orderUserInfo_id")
    private OrderUserInfo orderUserInfo;

    @Embedded
    private Address deliveryAddress;

    @Embedded
    private UserAddress userAddress;

    @OneToMany
    @JoinTable(name = "OrderItem", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "orderItem_id"))
    private List<OrderItem> orderItems;

    public Order() {
        this.creationDate = LocalDateTime.now();
    }

    public Order(OrderStatus status, OrderUserInfo orderUserInfo, String deliveryState, String deliveryTown,
            String deliveryStreet, String deliveryStreetNumber, String deliveryPostCode, String userState,
            String userTown, String userStreet, String userStreetNumber, String userPostCode,
            List<OrderItem> orderItems) {
        this();
        this.status = status;
        this.orderUserInfo = orderUserInfo;
        this.deliveryAddress = new Address(deliveryState, deliveryTown, deliveryStreet, deliveryStreetNumber,
                deliveryPostCode);
        this.userAddress = new UserAddress(userState, userTown, userStreet, userStreetNumber, userPostCode);
        this.orderItems = orderItems;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }

}