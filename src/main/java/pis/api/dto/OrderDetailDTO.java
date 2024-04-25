/*
 * PIS Projekt 2024
 * OrderDetailDTO.java
 * @author Filip Brna <xbrnaf00>
 */

package pis.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import pis.data.Order;
import pis.data.OrderStatus;
import pis.data.OrderUserInfo;
import pis.data.UserAddress;

public class OrderDetailDTO {
    private long id;

    private OrderStatus status;

    private LocalDateTime creationDate;

    private OrderUserInfo orderUserInfo;

    private Address deliveryAddress;

    private UserAddress userAddress;

    private List<pis.data.OrderItem> orderItems;

    private List<ModificationDTO> modifications;

    public OrderDetailDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.creationDate = order.getCreationDate();
        this.orderUserInfo = order.getOrderUserInfo();
        this.deliveryAddress = order.getDeliveryAddress();
        this.userAddress = order.getUserAddress();
        this.orderItems = order.getOrderItems();
        this.modifications = order.getModifications().stream().map(ModificationDTO::new).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public OrderUserInfo getOrderUserInfo() {
        return orderUserInfo;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public List<pis.data.OrderItem> getOrderItems() {
        return orderItems;
    }

    public List<ModificationDTO> getModifications() {
        return modifications;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setOrderUserInfo(OrderUserInfo orderUserInfo) {
        this.orderUserInfo = orderUserInfo;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public void setOrderItems(List<pis.data.OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setModifications(List<ModificationDTO> modifications) {
        this.modifications = modifications;
    }

}
