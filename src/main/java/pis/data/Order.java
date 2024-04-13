/*
 * PIS Projekt 2024
 * Order.java
 * @author Filip Brna <xbrnaf00>
 */
package pis.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    public Order() {
        this.creationDate = LocalDateTime.now();
    }

    public Order(OrderStatus status, OrderUserInfo orderUserInfo, String deliveryState, String deliveryTown,
            String deliveryStreet, String deliveryStreetNumber, String deliveryPostCode, String userState,
            String userTown, String userStreet, String userStreetNumber, String userPostCode) {
        this();
        this.status = status;
        this.orderUserInfo = orderUserInfo;
        this.deliveryAddress = new Address(deliveryState, deliveryTown, deliveryStreet, deliveryStreetNumber,
                deliveryPostCode);
        this.userAddress = new UserAddress(userState, userTown, userStreet, userStreetNumber, userPostCode);
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

}